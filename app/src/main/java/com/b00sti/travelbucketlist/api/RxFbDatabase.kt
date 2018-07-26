package com.b00sti.travelbucketlist.api

import com.b00sti.travelbucketlist.model.Bucket
import com.b00sti.travelbucketlist.utils.copyAndClear
import com.b00sti.travelbucketlist.utils.toArrayList
import com.google.firebase.database.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import java.util.*

/**
 * Created by b00sti on 08.02.2018
 */
object RxFbDatabase {

    //region Database contract and references
    private lateinit var db: FirebaseDatabase

    private const val MAIN = "main"
    private lateinit var dbMain: DatabaseReference

    private const val ALL_BUCKET_LISTS = "all_bucket_lists"
    private lateinit var dbAllBucketLists: DatabaseReference

    private const val PUBLIC_ITEMS = "all_to_dos"
    private lateinit var dbAllToDos: DatabaseReference
    //endregion

    fun setUpDatabase() {
        db = FirebaseDatabase.getInstance()
        db.setPersistenceEnabled(true)
        db.setLogLevel(Logger.Level.DEBUG)
        dbMain = db.reference.child(MAIN)
        dbAllBucketLists = dbMain.child(ALL_BUCKET_LISTS)
        dbAllToDos = dbMain.child(PUBLIC_ITEMS)
        dbMain.keepSynced(true)
    }

    //region Database functions
    fun addNewBucketList(bucketList: Bucket.BucketList): Completable = Completable.create({ emitter ->
        val map = WeakHashMap<String, Any>()
        bucketList.id = dbAllBucketLists.push().key
        map[bucketList.id] = bucketList
        dbAllBucketLists.updateChildren(map).addOnCompleteListener({ task ->
            if (task.isSuccessful) emitter.onComplete()
            else task.exception?.let { emitter.onError(it) }
        })
    })

    fun addNewBucketToDo(bucketToDo: Bucket.BucketToDo): Completable = Completable.create({ emitter ->
        val map = WeakHashMap<String, Any>()
        bucketToDo.id = dbAllToDos.push().key
        map[bucketToDo.id] = bucketToDo
        dbAllToDos.updateChildren(map).addOnCompleteListener({ task ->
            if (task.isSuccessful) emitter.onComplete()
            else task.exception?.let { emitter.onError(it) }
        })
    })

    fun assignToDoToBucketList(bucketToDo: Bucket.BucketToDo, bucketList: Bucket.BucketList): Completable = Completable.create({ emitter ->
        val map = WeakHashMap<String, Any>()
        map[bucketToDo.id] = bucketToDo.id
        dbAllBucketLists.child(bucketList.id).child("to_dos").updateChildren(map).addOnCompleteListener({ task ->
            if (task.isSuccessful) emitter.onComplete()
            else task.exception?.let { emitter.onError(it) }
        })
    })

    fun getBucketList(bucketList: Bucket.BucketList): Observable<ArrayList<Bucket.BucketToDo>> = Observable.create { emitter ->
        dbAllBucketLists.child(bucketList.id).child("to_dos").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError?) = handleDatabaseErrors(error, emitter)
            override fun onDataChange(data: DataSnapshot?) {
                when {
                    isEmpty(data) -> emitter.onNext(arrayListOf())
                    else          -> getPaginedItems(data, emitter, ::getBucketItem)
                }
            }
        })
    }

    private fun getBucketItem(key: String): Single<Bucket.BucketToDo> = Single.create { emitter ->
        dbAllToDos
                .child(key)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError?) {
                        if (error != null) emitter.onError(error.toException())
                        else emitter.onError(NullPointerException("Canceled with null value!"))
                    }

                    override fun onDataChange(data: DataSnapshot?) {
                        val toDo: Bucket.BucketToDo = Bucket.BucketToDo("", "", 1, "")
                        when {
                            data == null -> emitter.onSuccess(toDo)
                            else         -> {
                                /*for (entry in data.children) {
                                    try {
                                        // entry.key
                                        // entry.getValue(BucketToDo::class.java)?.let { items.add(it) }
                                    } catch (e: DatabaseException) {
                                        e.printStackTrace()
                                    }
                                }*/
                                data.getValue(Bucket.BucketToDo::class.java)?.let {
                                    emitter.onSuccess(it)
                                }
                            }
                        }
                    }
                })
    }
    //endregion

    //region Utils
    private fun <T> getPaginedItems(data: DataSnapshot?, emitter: ObservableEmitter<ArrayList<T>>, fetchItem: (String) -> Single<T>, PAGINATION_SIZE: Int = 20) {
        val listOfQueries: MutableList<Single<T>> = mutableListOf()
        var counter = 1
        data!!.children.forEach { entry ->
            listOfQueries.add(fetchItem(entry.key))
            counter++
            if (counter > PAGINATION_SIZE) {
                counter = 1
                Single.zip<T, ArrayList<T>>(listOfQueries.copyAndClear(), { it.toArrayList() })
                        .subscribeBy { emitter.onNext(it) }
            }
        }
    }

    private fun isEmpty(data: DataSnapshot?): Boolean = data == null || data.childrenCount == 0L

    private fun handleDatabaseErrors(error: DatabaseError?, emitter: ObservableEmitter<ArrayList<Bucket.BucketToDo>>) {
        if (error != null) emitter.onError(error.toException())
        else emitter.onError(NullPointerException("Canceled with null value!"))
    }
    //endregion

}
