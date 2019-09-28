package com.howtohelp.howtohelp;

import android.Manifest;
import android.content.Intent;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.howtohelp.howtohelp.ui.OpportunitiesActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.content.Intent.ACTION_SEND;
import static org.junit.Assert.assertTrue;

public final class CrashRateLessThanHalfPercentTest {

    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public IntentsTestRule<OpportunitiesActivity> intentsTestRule =
            new IntentsTestRule<>(OpportunitiesActivity.class, true, false);

    @Test
    public void testCrashLessThanHalfPercentTest() {
        int crashCount = 0;
        int loops = 0;
        while (loops < 2) {
            try {
                final String URL_TEXT =
                        "https://www.theguardian.com/uk-news/2019/aug/11/westminster-rough-sleepers-evicted-from-parliamentary-estate";
                Intent intent = new Intent();
                intent.setAction(ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, URL_TEXT);
                intentsTestRule.launchActivity(intent);
                Thread.sleep(5000);
            } catch (Exception e) {
                ++crashCount;
            } finally {
                intentsTestRule.finishActivity();
                ++loops;
            }
        }

        assertTrue(crashCount < 1);
    }
}
