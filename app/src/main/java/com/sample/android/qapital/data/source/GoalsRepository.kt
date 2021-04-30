package com.sample.android.qapital.data.source

import com.sample.android.qapital.data.SavingsGoal
import com.sample.android.qapital.data.source.local.LocalDataSource
import com.sample.android.qapital.network.QapitalService
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalsRepository @Inject constructor(
        private val remoteDataSource: QapitalService,
        private val localDataSource: LocalDataSource,
) : BaseRepository<List<SavingsGoal>>() {

    override val resultRemoteDataSource: Observable<List<SavingsGoal>>
        get() = remoteDataSource.requestSavingGoals().map { it.wrapper }.flatMap {
            localDataSource.insertAll(it).andThen(Observable.fromCallable { it })
        }.doOnComplete { cacheIsDirty = false }

    override val resultLocalDataSource: Observable<List<SavingsGoal>>
        get() = localDataSource.getSavingsGoals()
}
