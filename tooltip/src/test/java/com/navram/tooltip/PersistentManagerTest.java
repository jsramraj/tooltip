package com.navram.tooltip;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PersistentManagerTest {
    @Mock
    Context context;
    @Mock
    SharedPreferences sharedPreferences;
    @Mock
    SharedPreferences.Editor editor;

    @Before
    public void setup() {
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.commit()).thenReturn(true);

        Map<String, String> map = new HashMap<>();

        when(sharedPreferences.getBoolean(anyString(), anyBoolean())).thenAnswer((Answer<Boolean>) invocation -> {
            String key = invocation.getArgument(0);
            return map.containsKey(key);
        });
        when(sharedPreferences.getAll()).thenAnswer((Answer<Map<String, String>>) invocation -> {
            Map<String, String> copy = new HashMap<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                copy.put(entry.getKey(), entry.getValue());
            }
            return copy;
        });
        when(editor.putBoolean(anyString(), anyBoolean())).thenAnswer((Answer<SharedPreferences.Editor>) invocation -> {
            String key = invocation.getArgument(0);
            String value = invocation.getArgument(0);
            map.put(key, value);
            return null;
        });
        when(editor.remove(anyString())).thenAnswer((Answer<SharedPreferences.Editor>) invocation -> {
            Iterator<String> iterator = map.keySet().iterator();
            String key = invocation.getArgument(0);

            while (iterator.hasNext()) {
                String iter = iterator.next();
                if (iter.startsWith(key)) {
                    iterator.remove();
                }
            }
            return null;
        });

        PersistentManager.getInstance().init(context);
    }

    @Test
    public void constructor_Returns_ValidInstance() {
        assertNotNull(PersistentManager.getInstance());
    }

    @Test
    public void byDefault_TipIsNotAcknowledged() {
        assertFalse(PersistentManager.getInstance().isAcknowledged(anyString(), anyInt()));
    }

    @Test
    public void acknowledgingATip_AddsAnEntryInSharedPreferences() {
        PersistentManager.getInstance().acknowledge("Mainactivity", 123);

        assertTrue(PersistentManager.getInstance().isAcknowledged("Mainactivity", 123));
    }

    @Test
    public void resettingAcknowledgementForATip_RemovesTheEntryInSharedPreferences() {
        PersistentManager.getInstance().acknowledge("MainActivity", 123);
        assertTrue(PersistentManager.getInstance().isAcknowledged("MainActivity", 123));

        PersistentManager.getInstance().resetAcknowledgement("MainActivity", 123);
        assertFalse(PersistentManager.getInstance().isAcknowledged("MainActivity", 123));
    }

    @Test
    public void resettingAcknowledgementForAllTipsInAnActivity_RemovesEntries() {
        PersistentManager.getInstance().acknowledge("MainActivity", 1);
        PersistentManager.getInstance().acknowledge("MainActivity", 2);
        PersistentManager.getInstance().acknowledge("LoginActivity", 3);

        assertTrue(PersistentManager.getInstance().isAcknowledged("MainActivity", 1));
        assertTrue(PersistentManager.getInstance().isAcknowledged("LoginActivity", 3));

        PersistentManager.getInstance().resetAcknowledgementForActivity("MainActivity");

        assertFalse(PersistentManager.getInstance().isAcknowledged("MainActivity", 123));
        assertTrue(PersistentManager.getInstance().isAcknowledged("LoginActivity", 3));
    }

}
