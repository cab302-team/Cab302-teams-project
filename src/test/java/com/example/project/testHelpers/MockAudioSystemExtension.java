package com.example.project.testHelpers;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.MockedStatic;

import javax.sound.sampled.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.mockito.Mockito.*;

/**
 * Automatically mocks AudioSystem for CI tests.
 */
public class MockAudioSystemExtension implements BeforeEachCallback, AfterEachCallback{

    private static MockedStatic<AudioSystem> mockedAudioSystem;

    @Override
    public void beforeEach(ExtensionContext context)
    {
        if (mockedAudioSystem == null) {
            mockedAudioSystem = mockStatic(AudioSystem.class);

            // Return a mock Clip for getClip()
            var mockClip = mock(Clip.class);
            mockedAudioSystem.when(AudioSystem::getClip).thenReturn(mockClip);

            FloatControl mockControl = mock(FloatControl.class);
            when(mockClip.getControl(any(FloatControl.Type.MASTER_GAIN.getClass()))).thenReturn(mockControl);

            // Return a dummy AudioInputStream for any InputStream
            mockedAudioSystem.when(() ->
                    AudioSystem.getAudioInputStream(org.mockito.ArgumentMatchers.any(InputStream.class))
            ).thenAnswer(invocation -> new AudioInputStream(
                    new ByteArrayInputStream(new byte[0]),
                    new AudioFormat(44100f, 16, 2, true, false),
                    0
            ));

            // Also return dummy conversion for AudioInputStream -> AudioInputStream
            mockedAudioSystem.when(() ->
                    AudioSystem.getAudioInputStream(org.mockito.ArgumentMatchers.any(AudioFormat.class),
                            org.mockito.ArgumentMatchers.any(AudioInputStream.class))
            ).thenAnswer(invocation -> invocation.getArgument(1));
        }
    }

    @Override
    public void afterEach(ExtensionContext extensionContext){
        if (mockedAudioSystem != null) {
            mockedAudioSystem.close();
            mockedAudioSystem = null;
        }
    }
}
