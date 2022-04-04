## MotionLayout
[MotionLayout](https://developer.android.com/training/constraint-layout/motionlayout?hl=ko)은 앱에서 모션과 위젯 애니메이션을 관리하는 데 사용할 수 있는 레이아웃으로 MotionLayout은 ConstraintLayout의 서브ㅡ클래스이며 제약 설정과 같은 다양한 레이아웃 기능들이 포함되어 있습니다. ConstraintLayout 라이브러리 일부인 MotionLayout은 추가 라이브러리 설정 없이 바로 사용 가능합니다.

<br>

### MotionLayout 시작하기
1. ConstraintLayout 라이브러리 추가 : 기본으로 제공
2. MotionLayout 파일 만들기 : ConstraintLayout의 서브클래스이므로 이름만 변경
3. MotionScene 만들기 : app:layoutDescription 속성에 입력할 MotionScence 참조

<br>

> MotionScene이란? <br>
MotionLayout에서 app:layoutDescription 속성은 MotionScence 참조, MotionScence은 레이아웃의 모든 모션 설명을 포함하는 XML 리소스 파일
<br>

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<MotionScene 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:motion="http://schemas.android.com/apk/res-auto">

    <Transition
        motion:constraintSetEnd="@+id/end"
        motion:constraintSetStart="@id/start"
        motion:duration="1000">
       <KeyFrameSet>
       </KeyFrameSet>
    </Transition>

    <ConstraintSet android:id="@+id/start">
        <Constraint
            android:id="@+id/mainBottomNavigationView"
            motion:layout_constraintEnd_toEndOf="parent"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            motion:layout_constraintBottom_toBottomOf="parent"
            motion:layout_constraintStart_toStartOf="parent" />
    </ConstraintSet>

    <ConstraintSet android:id="@+id/end">
        <Constraint
            android:id="@+id/mainBottomNavigationView"
            motion:layout_constraintEnd_toEndOf="parent"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:translationY="56dp"
            motion:layout_constraintBottom_toBottomOf="parent"
            motion:layout_constraintStart_toStartOf="parent" />
    </ConstraintSet>
</MotionScene>
```

- Transition 속성 : 모션 정의
   
    - motion:constraintSetStart, motion:constraintSetEnd 모션의 시작과 끝 참조하여 해당 모션에서 정의된 ContraintSet 설정, MotionScene의 ConstraintSet의 id로 통해서 지정 가능
    
    - motion:duration : 모션이 완료되는 데 걸리는 시간 지정

- OnSwipe : 터치를 통해 모션 제어

    - motion:touchAnchorId : 스와이프하고 드래그 할 수 있는 뷰를 가리킴
    - motion:touchAnchorSide : 오른쪽에서 보기를 드래그
    - motaion:dragDirection : 드래그 진행 방향

- ConstraintSet : 모션을 설명하는 다양한 제약조건 정의

- KeyFrameSet : 모션 애니메이션 과정에서 뷰의 위치(KeyPosition) 및 속성(KeyAttribute) 지정


<br>

## Exoplayer
[Exoplayer](https://exoplayer.dev/hello-world.html)는 안드로이드를 위한 애플리케이션 레벨의 미디어 플레이어입니다.Exoplayer는 로컬 또는 네트워크를 통한 오디오, 비디어를 재생할 수 있는 API를 제공해줍니다. Exoplayer는 Android Media Player에서 지원하니 않는 DASH, SmoothStreaming 등을 지원합니다. 또한 MediaPlayer API와 달리 Exoplayer는 커스터마이징과 확장이 용이합니다.

<br>

### Exoplayer 시작하기
1. 프로젝트에 Exoplayer 종속성 추가
2. Exoplayer 인스턴스 생성
3. PlayerView에 Player 설정(사용자에게 보여질 어떤 뷰에 Player 지정할지 결정)
4. 재생될 MediaItem 준비
5. 재생을 마칠 경우 메모리 릭 방지를 위해 리소스 해제
