package com.decode.mybooksummaries.data.repository

import android.util.Log
import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.data.local.dao.QuoteDao
import com.decode.mybooksummaries.data.mapper.toQuoteEntity
import com.decode.mybooksummaries.domain.model.Quote
import com.decode.mybooksummaries.domain.repository.QuoteRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class QuoteRepositoryImpl @Inject constructor(
    @Named("quotesRef") private val quotesRef: CollectionReference,
    private val quotesDao: QuoteDao
) : QuoteRepository {
    override suspend fun addQuote(
        quote: Quote,
        isConnected: Boolean
    ): Response<Quote> {
        return try {
            val quoteEntity = quote.toQuoteEntity().copy(isSynced = isConnected)
            quotesDao.insertQuote(quoteEntity)
            if (isConnected) quotesRef.document(quote.id).set(quote).await()
            Response.Success(quote)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "Error adding book")
        }
    }

    override fun getQuotes(
        bookId: String,
        isConnected: Boolean
    ): Flow<Response<List<Quote>>> = callbackFlow {
        Log.d("Firestore", "Fetching quotes for bookId: $bookId")
        val listener = quotesRef
            .whereEqualTo("bookId", bookId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Response.Failure(error.message ?: "Error fetching quotes"))
                    Log.d("QuoteRepository", "Error fetching quotes: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val firestoreQuotes = snapshot.documents.mapNotNull { it.toObject(Quote::class.java) }
                    CoroutineScope(Dispatchers.IO).launch {
                        quotesDao.insertQuotes(firestoreQuotes.map {
                            it.toQuoteEntity().copy(isSynced = true)
                        })
                    }
                    trySend(Response.Success(firestoreQuotes))
                } else {
                    trySend(Response.Empty)
                }
            }

        awaitClose { listener.remove() }
    }

    override suspend fun deleteQuote(
        quote: Quote,
        isConnected: Boolean
    ): Response<String> {
        return try {
            quotesDao.deleteQuote(quote.id)
            if (isConnected){
                val quotesSnapshot = quotesRef.whereEqualTo("quote", quote.quote).get().await()
                quotesSnapshot.forEach {
                    it.reference.delete().await()
                }
            }
            Response.Success("Book deleted successfully")
        } catch (e: Exception) {
            Response.Failure(e.message ?: "Error deleting book")
        }
    }
}