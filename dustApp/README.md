## AppWidget
앱 위젯은 다른 애플리케이션(홈 화면)에 삽입되어 주기적인 업데이트를 받을 수 있는 소형 애플리케이션 뷰.<br>
이를 Widget이라고 하며 WidgetProvider를 통해 생성할 수 있습니다.<br>
다른 앱 위젯을 포함하는 구성요소를 AppWidgetHost 라고 합니다.<br>

<br>

### 기본 사항
- AppWidgetProviderInfo : 레이아웃, 업데이트 빈도 등 메타데이터 설명
- AppWidgetProvider : 브로드캐스트 이벤트 기반으로 위젯 업데이트, 사용설정, 사용중지, 삭제될 때 수신

<br>

### manifest에서 위젯 설정
```kotlin
        <receiver android:name=".appwidget.SimpleAirQualityWidgetProvider">
            <intent-filter>
                <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
            </intent-filter>
            <meta-data
                android:name="android.appwidget.provider"
                android:resource="@xml/widget_simple_info" />
        </receiver>
```
- <receiver> 요소에 AppWidgetProvider를 지정하는 android:name 속성 필수
- <intent-filter> 에도 name 속성이 있는 <action> 요소 포함
- AppWigetManager는 다른 모든 앱 위젯 브로드캐스트를 AppWidgetProvider로 전송
- <meta-data> 요소는 AppWidgetProviderInfo 리소스 지정
	- name : android.appwidget.provider 지정
	- resource : AppWidgetProviderInfo 리소스 위치 지정

<br>

### AppWidgetProviderInfo 메타데이터 추가
```kotlin
<?xml version="1.0" encoding="utf-8"?>
<appwidget-provider xmlns:android="http://schemas.android.com/apk/res/android"
    android:initialLayout="@layout/widget_simple"
    android:minWidth="110dp"
    android:minHeight="50dp"
    android:resizeMode="none"
    android:updatePeriodMillis="3600000"
    android:widgetCategory="home_screen" />
```
- 최소 레이아웃 크기, 초기 레이아웃 리소스, 앱 위젯 업데이트 빈도
- <appwidget-provider> 태그 사용하여 객체 정의
- resizemode : 위젯의 크기를 조절할 수 있는 규칙을 지정
- widgetCategory : 홈 화면, 잠금화면 또는 둘다에 표시할 수 있는지 여부 선언, 5.0미만에만 잠금 화면 위젯 지원

<br>

### AppWidgetProvider 클래스 사용
AppWidgetProvider 클래스는 BoardCastReceiver를 상속받아 이벤트를 처리하기 위한 편의성 클래스<br>
AppWidgetProvider는 앱 위젯이 업데이트, 삭제, 사용 설정 및 사용 중지되는 경우와 같이 위젯과 관련성 높은 이벤트 브로드캐스트만 수신

<br>

#### onUpdate()
AppWidgetProviderInfo의 updatePeriodMills 속성에 의해 정의된 간격으로 앱 위젯을 업데이트 하기 위해 호출<br>
이 메서드는 사용자가 앱 위젯을 추가할 떄도 호출되므로 뷰 이벤트 핸들러 정의 및 필수 설정하고 임시 Service 시작 <br>

<br>

#### RemoteView
앱 위젯 레이아웃은 특정 종류의 레이아웃 또는 위젯만 지원하는 RemoteViews를 기반<br>
RemoteView는 다른 프로세에 보여질수 있도록 하는 클래스

<br>

#### 위젯 업데이트
AppWidgetManager에게 직접 업데이트 요청
- AppWidgetManager 인스턴스 생성
- updateAppWidget(int, RemoteViews) 호출하여 RemoteView 레이아웃으로 앱 위젯 업데이트

<br><br>

## Foreground Service
Android O(API 26)부터 백그라운드 서비스 실행이 제한되어 Foreground로 실행 필수 <br>
BroadcastReceiver와 같은 Foregounrd가 아닌 상태에서도 백그라운드 서비스가 아닌 포그라운드 서비스로 실행해야함<br>
Foreground 실행시키기 위해서 서비스가 실행중이라는 내용의 Notification이 등록되어 사용자가 인지해야 함<br>

