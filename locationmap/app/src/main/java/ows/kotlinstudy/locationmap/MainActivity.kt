package ows.kotlinstudy.locationmap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import kotlinx.coroutines.*
import ows.kotlinstudy.locationmap.MapActivity.Companion.SEARCH_RESULT_EXTRA_KEY
import ows.kotlinstudy.locationmap.databinding.ActivityMainBinding
import ows.kotlinstudy.locationmap.model.LocationLatLngEntity
import ows.kotlinstudy.locationmap.model.SearchResultEntity
import ows.kotlinstudy.locationmap.response.search.Poi
import ows.kotlinstudy.locationmap.response.search.Pois
import ows.kotlinstudy.locationmap.utility.RetrofitUtil
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job

    /**
     * CoroutineContext plus 연산자 정의되어 있음.
     * Dispathers.Main : 메인스레드에 코루틴 실행 요청
     * job : Job 객체를 하나 생성하여 하위의 Job 객체들이 여러 생성이 되어서 해당 job cancel 시 전체 job 종료
     * -> structured concurrency
     */
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SearchRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()
        initAdapter()
        initViews()
        bindViews()
        initData()
    }

    private fun initViews() = with(binding) {
        emptyResultTextView.isVisible = false
        recyclerView.adapter = adapter
    }

    private fun bindViews() = with(binding) {
        searchButton.setOnClickListener {
            searchKeyword(searchEditText.text.toString())
        }
    }

    private fun initAdapter() {
        adapter = SearchRecyclerAdapter()
    }

    private fun initData() {
        adapter.notifyDataSetChanged()
    }

    private fun setData(pois: Pois) {
        val dataList = pois.poi.map {
            SearchResultEntity(
                name = it.name ?: "없음",
                fullAddress = makeMainAdress(it),
                locationLatLng = LocationLatLngEntity(
                    it.noorLat,
                    it.noorLon
                )
            )
        }

        adapter.setSearhResult(dataList) {
            startActivity(
                Intent(this, MapActivity::class.java).apply {
                    putExtra(SEARCH_RESULT_EXTRA_KEY, it)
                }
            )
        }
    }

    private fun searchKeyword(keywordString: String) {
        launch(coroutineContext) {
            try {
                /**
                 * 원래 네트워크 요청은 Dispatchers.IO에게 하는게 맞으나, retrofit은 자체적으로 비동기 처리하기 때문에
                 * 디스패처 설명할 필요없다. -> 자체 Dispatcher 사용
                 */
                val response = RetrofitUtil.apiService.getSearchLocation(
                    keyword = keywordString
                )

                if (response.isSuccessful) {
                    val body = response.body()

                    /**
                     * 이미 현재가 Dispathers.Main
                     */
                    body?.let { searchResponse ->
                        Log.d("msg",body.toString())
                        setData(searchResponse.searchPoiInfo.pois)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, "검색 에러 : ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun makeMainAdress(poi: Poi): String =
        if (poi.secondNo?.trim().isNullOrEmpty()) {
            (poi.uppderAddrName?.trim() ?: "") + " " +
                    (poi.middleAddrName?.trim() ?: "") + " " +
                    (poi.lowerAddrName?.trim() ?: "") + " " +
                    (poi.detailAddrName?.trim() ?: "") + " " +
                    poi.firstNo?.trim()
        } else {
            (poi.uppderAddrName?.trim() ?: "") + " " +
                    (poi.middleAddrName?.trim() ?: "") + " " +
                    (poi.lowerAddrName?.trim() ?: "") + " " +
                    (poi.detailAddrName?.trim() ?: "") + " " +
                    (poi.firstNo?.trim() ?: "") + " " +
                    poi.secondNo?.trim()
        }
}