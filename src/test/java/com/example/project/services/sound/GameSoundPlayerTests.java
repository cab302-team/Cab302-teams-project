package com.example.project.services.sound;

import com.example.project.services.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Tests for {@link GameSoundPlayer}
 */
public class GameSoundPlayerTests
{
    private final String validPath = "/com/example/project/Sounds/clack3.wav";

    @Test
    void test_GameSoundPlayer_success()
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

            var soundPlayer = new GameSoundPlayer(validPath, new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream()));
        }
    }

    @Test
    void constructor()
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

            var soundPlayer = new GameSoundPlayer(validPath, new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream()), -6f);
            verify(mockClip).getControl(any(Control.Type.class));
            verify(mockControl).setValue(-6f);
        }
    }

    @Test
    void constructor_throwsIllegalArgumentException()
    {
        var path = "invalid-path";
        var log =  new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream());
        assertThrows(IllegalArgumentException.class, () -> new GameSoundPlayer(path, log));
    }

    @Test
    void constructor_throwsUnsupportedException()
    {
        try (MockedStatic<AudioSystem> mockedAudioSystem = mockStatic(AudioSystem.class))
        {
            mockedAudioSystem.when(() -> AudioSystem.getAudioInputStream(any(InputStream.class)))
                    .thenThrow(new UnsupportedAudioFileException("Unsupported format"));

            var exception = assertThrows(IllegalArgumentException.class, () -> new GameSoundPlayer(validPath, new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream()), 0f));
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

            var newMusic = new GameSoundPlayer(validPath, new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream()), 0f);
            newMusic.mute();

            verify(mockClip, times(2)).getControl(any(Control.Type.class));
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

            var newMusic = new GameSoundPlayer(validPath, new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream()), 0f);
            newMusic.unMute();

            // assert that clip.getControl(FloatControl.Type.MASTER_GAIN); was called
            // and that gain was set to 0.
            verify(mockClip, times(2)).getControl(any(Control.Type.class));
            verify(mockControl, times(2)).setValue(0f);
        }
    }
}
