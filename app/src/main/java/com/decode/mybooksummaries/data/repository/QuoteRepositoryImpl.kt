package com.decode.mybooksummaries.data.repository

import android.util.Log
import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.data.local.db.BookDatabase
import com.decode.mybooksummaries.data.mapper.toQuoteEntity
import com.decode.mybooksummaries.domain.model.Quote
import com.decode.mybooksummaries.domain.repository.QuoteRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuoteRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val db: BookDatabase
) : QuoteRepository {

    private val quotesRef: CollectionReference
        get() = firestore.collection("users").document(auth.currentUser?.uid ?: "")
            .collection("quotes")

    override suspend fun addQuote(
        quote: Quote,
        isConnected: Boolean
    ): Response<Quote> {
        return try {
            if (isConnected) {
                db.quoteDao().insertQuote(quote.toQuoteEntity())
                quotesRef.document(quote.id).set(quote.copy(userId = auth.currentUser?.uid)).await()
                Response.Success(quote)
            } else Response.Failure("No internet connection")
        } catch (_: Exception) {
            Response.Failure("An error occurred while adding a book")
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
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(Response.Failure(error.message ?: "Error fetching quotes"))
                        return@addSnapshotListener
                    }

                    val firestoreQuotes = snapshot?.documents
                        ?.mapNotNull { it.toObject(Quote::class.java) }
                        .orEmpty()

                    CoroutineScope(Dispatchers.IO).launch {
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
            trySend(Response.Failure("No internet connection"))
            awaitClose()
        }
    }


    override suspend fun deleteQuote(
        quote: Quote,
        isConnected: Boolean
    ): Response<String> {
        return try {
            if (isConnected) {
                db.quoteDao().deleteQuote(quote.id)
                val quotesSnapshot =
                    quotesRef.whereEqualTo("quote", quote.quote).limit(1).get().await()
                quotesSnapshot.documents.firstOrNull()?.reference?.delete()?.await()
                Response.Success("Book deleted successfully")
            } else Response.Failure("No internet connection")
        } catch (e: Exception) {
            Response.Failure(e.message ?: "Error deleting book")
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