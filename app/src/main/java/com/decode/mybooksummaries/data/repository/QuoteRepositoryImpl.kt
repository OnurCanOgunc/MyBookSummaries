package com.decode.mybooksummaries.data.repository

import android.util.Log
import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.data.local.db.BookDatabase
import com.decode.mybooksummaries.data.mapper.toQuote
import com.decode.mybooksummaries.data.mapper.toQuoteEntity
import com.decode.mybooksummaries.di.IoDispatcher
import com.decode.mybooksummaries.domain.model.Quote
import com.decode.mybooksummaries.domain.repository.QuoteRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuoteRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val db: BookDatabase,
    @IoDispatcher private val ioScope: CoroutineScope
) : QuoteRepository {

    private val quotesRef: CollectionReference
        get() = firestore.collection("users").document(auth.currentUser?.uid ?: "")
            .collection("quotes")

    override suspend fun addQuote(
        quote: Quote,
        isConnected: Boolean
    ): Response<Quote> {
        return runCatching {
            val quoteEntity = quote.toQuoteEntity().copy(isSynced = isConnected)
            db.quoteDao().insertQuote(quoteEntity)
            if (isConnected) quotesRef.document(quote.id)
                .set(quote.copy(userId = auth.currentUser?.uid)).await()
            Response.Success(quote)

        }.getOrElse {
            Response.Failure("An error occurred while adding a quote")
        }
    }

    override fun getQuotes(
        bookId: String,
        isConnected: Boolean
    ): Flow<Response<List<Quote>>> = callbackFlow {
        val userId = auth.currentUser?.uid ?: return@callbackFlow
        Log.d("firestore", "Fetching quotes for userId: $userId, bookId: $bookId")

        if (isConnected) {
            val listener = quotesRef
                .whereEqualTo("bookId", bookId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(Response.Failure(error.message ?: "Error fetching quotes"))
                        return@addSnapshotListener
                    }

                    val firestoreQuotes = snapshot?.documents
                        ?.mapNotNull { it.toObject(Quote::class.java) }
                        .orEmpty()

                    ioScope.launch {
                        db.quoteDao().insertQuotes(firestoreQuotes.map { it.toQuoteEntity() })
                    }

                    trySend(
                        if (firestoreQuotes.isEmpty()) Response.Empty else Response.Success(
                            firestoreQuotes
                        )
                    )
                }

            awaitClose { listener.remove() }
        } else {
            val localQuotes = db.quoteDao().getQuotes(bookId).first().map { it.toQuote() }
            trySend(if (localQuotes.isEmpty()) Response.Empty else Response.Success(localQuotes))
            awaitClose()
        }
    }


    override suspend fun deleteQuote(
        quote: Quote,
        isConnected: Boolean
    ): Response<String> {
        return runCatching {
            if (isConnected) {
                db.quoteDao().deleteQuote(quote.id)
                quotesRef.document(quote.id).delete().await()
                Log.d("firestore", "Deleting quote with id: ${quote.id}")
                Response.Success("Quote deleted successfully")
            } else db.quoteDao().markQuoteAsDeleted(quote.id)
            Response.Success("The deletion request has been recorded and will be completed once the internet connection is restored.")
        }.getOrElse {
            Response.Failure(it.message ?: "Error deleting book")
        }
    }

    override suspend fun deleteAllQuotes(isConnected: Boolean): Response<String> {
        return runCatching {
            db.quoteDao().deleteAllQuotes()
            if (isConnected) {
                val snapshot = quotesRef.get().await()
                snapshot.forEach {
                    it.reference.delete().await()
                }
            }
            Response.Success("Quotes deleted successfully")
        }.getOrElse {
            Response.Failure(it.message ?: "Error deleting quotes")
        }
    }
}