package ows.kotlinstudy.melon

import android.util.Log
import ows.kotlinstudy.melon.service.MusicDto
import ows.kotlinstudy.melon.service.MusicEntity

fun MusicEntity.mapper(id: Long): MusicModel =
    MusicModel(
        id = id,
        streamUrl = streamUrl,
        coverUrl = coverUrl,
        track = track,
        artist = artist
    )

fun MusicDto.mapper(): PlayerModel =
    PlayerModel(
        playMusicList = musics.mapIndexed { index, musicEntity ->
            Log.d("msg","playMusicList $index")
            musicEntity.mapper(index.toLong())
        })