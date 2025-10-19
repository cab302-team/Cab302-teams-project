package com.example.project.services.sound;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


/**
 * Tests for the class {@link GameMusicPlayer}
 */
public class GameMusicPlayerTests
{
    @Test
    void test_createdGameMusic_success()
    {
        try (MockedStatic<AudioSystem> mockedAudioSystem = mockStatic(AudioSystem.class))
        {
            Clip mockClip = mock(Clip.class);
            AudioInputStream mockOriginalStream = mock(AudioInputStream.class);
            AudioInputStream mockConvertedStream = mock(AudioInputStream.class);
            AudioFormat mockFormat = mock(AudioFormat.class);

            mockedAudioSystem.when(() -> AudioSystem.getAudioInputStream(any(BufferedInputStream.class)))
                    .thenReturn(mockOriginalStream);
            mockedAudioSystem.when(() -> AudioSystem.getAudioInputStream(any(AudioFormat.class), any(AudioInputStream.class)))
                    .thenReturn(mockConvertedStream);
            mockedAudioSystem.when(AudioSystem::getClip)
                    .thenReturn(mockClip);

            when(mockOriginalStream.getFormat()).thenReturn(mockFormat);

            FloatControl control = mock(FloatControl.class);
            when(mockClip.getControl(FloatControl.Type.MASTER_GAIN)).thenReturn(control);

            var newMusic = new GameMusicPlayer();
            verify(control).setValue(-3);
        }
    }

    @Test
    void test_playGameMusicLoop()
    {
        try (MockedStatic<AudioSystem> mockedAudioSystem = mockStatic(AudioSystem.class))
        {
            Clip mockClip = mock(Clip.class);
            AudioInputStream mockOriginalStream = mock(AudioInputStream.class);
            AudioInputStream mockConvertedStream = mock(AudioInputStream.class);
            AudioFormat mockFormat = mock(AudioFormat.class);

            mockedAudioSystem.when(() -> AudioSystem.getAudioInputStream(any(BufferedInputStream.class)))
                    .thenReturn(mockOriginalStream);
            mockedAudioSystem.when(() -> AudioSystem.getAudioInputStream(any(AudioFormat.class), any(AudioInputStream.class)))
                    .thenReturn(mockConvertedStream);
            mockedAudioSystem.when(AudioSystem::getClip)
                    .thenReturn(mockClip);

            when(mockOriginalStream.getFormat()).thenReturn(mockFormat);

            FloatControl control = mock(FloatControl.class);
            when(mockClip.getControl(FloatControl.Type.MASTER_GAIN)).thenReturn(control);

            var newMusic = new GameMusicPlayer();
            newMusic.playGameMusicLoop();
            verify(mockClip).loop(Clip.LOOP_CONTINUOUSLY);
            verify(control).setValue(-3);
        }
    }
}