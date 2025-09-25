package com.example.project.services;

import com.example.project.services.sound.GameSoundPlayer;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Tests for {@link GameSoundPlayer}
 */
public class GameSoundPlayerTests
{
    private final String validPath = "/com/example/project/Sounds/Clack1.wav";


    @Test
    void test_GameSoundPlayer_success()
    {
        var soundPlayer = new GameSoundPlayer(validPath);
    }

    @Test
    void test_GameSoundPlayerWithVolume_success()
    {
        var validPath = "/com/example/project/Sounds/Clack1.wav";
        var soundPlayer = new GameSoundPlayer(validPath, -6f);
    }

    @Test
    void test_GameSoundPlayer_throwsIllegalArgumentException()
    {
        var path = "invalid-path";
        assertThrows(IllegalArgumentException.class, () -> new GameSoundPlayer(path));
    }

    @Test
    void test_GameSoundPlayer_AudioSystemThrowsUnsupportedAudioFileException()
    {
        try (MockedStatic<AudioSystem> mockedAudioSystem = mockStatic(AudioSystem.class))
        {
            mockedAudioSystem.when(() -> AudioSystem.getAudioInputStream(any(InputStream.class)))
                    .thenThrow(new UnsupportedAudioFileException("Unsupported format"));

            var exception = assertThrows(IllegalArgumentException.class, () -> new GameSoundPlayer(validPath));
            assertEquals("Error loading sound file: Unsupported format", exception.getMessage());
        }
    }

    @Test
    void test_mute(){
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

            // return fake control
            FloatControl mockControl = mock(FloatControl.class);
            when(mockClip.getControl(any(FloatControl.Type.MASTER_GAIN.getClass()))).thenReturn(mockControl);

            var newMusic = new GameSoundPlayer(validPath);
            newMusic.mute();

            verify(mockClip).getControl(any(Control.Type.class));
            verify(mockControl).setValue(-80f);
        }
    }

    @Test
    void test_unMute()
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

            // return fake control
            FloatControl mockControl = mock(FloatControl.class);
            when(mockClip.getControl(any(FloatControl.Type.MASTER_GAIN.getClass()))).thenReturn(mockControl);

            var newMusic = new GameSoundPlayer(validPath);
            newMusic.unMute();

            // assert that clip.getControl(FloatControl.Type.MASTER_GAIN); was called
            // and that gain was set to 0.
            verify(mockClip).getControl(any(Control.Type.class));
            verify(mockControl).setValue(0f);
        }
    }
}
