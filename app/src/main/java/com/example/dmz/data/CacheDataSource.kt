package com.example.dmz.data

import android.util.Log
import com.example.dmz.data.model.Keywords
import com.example.dmz.data.model.Quiz

class CacheDataSource {
    companion object {
        private var INSTANCE: CacheDataSource? = null
        fun getCacheDataSource(): CacheDataSource {
            /*- DataSource::class 객체에 lock을 걸어 한번에 한 스레드에서만 실행 되도록 함
              - 여러 스레드가 동시에 instance 생성 블록에 접근하지 못 하도록 함
            */
            return synchronized(CacheDataSource::class) {
                val newInstance = INSTANCE ?: CacheDataSource()
                INSTANCE = newInstance
                newInstance
            }
        }
    }

    fun getKeywordsList(): List<Keywords> {
        val data = keywordsList() // keywordsList()가 무엇을 반환하는지 확인
        Log.d("CacheDataSource", "$data")
        return data
    }

    fun getQuizList(): List<Quiz> {
        return quizList()
    }
}