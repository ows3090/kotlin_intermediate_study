## Google Map
- SupportMapFragment : Framgnent 정적/동적 등록 방법 존재
- OnMapReadyCallback : onMapReady 메소드 구현
- Marker : Marker.Options에 position, title, snippet 등 정보 저장
- CameraUpdateFatory : 카메가 원하는 위치로 이동

<br>

## LocationManager
- Permission ; ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION
- LocationListener : Location 변경 콜백 구현, requestLocationUpdates를 통해 Provider로부터 위치 등록

<br>

## Retrofit + Coroutine
- Coroutine은 CoroutineScope 내에서 실행
- CoroutineScope내에는 코루틴 수행하기 위한 정보인 CoroutineContext 존재
- CoroutineContext plus 연산자 가능
- Dispatchers.Main + job : job객체를 생성함으로써 Structered concurrency 구현 
