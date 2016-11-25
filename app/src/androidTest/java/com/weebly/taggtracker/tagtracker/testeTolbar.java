package com.weebly.taggtracker.tagtracker;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class testeTolbar {

    @Rule
    public ActivityTestRule<TelaInicialActivity> mActivityTestRule = new ActivityTestRule<>(TelaInicialActivity.class);

    @Test
    public void testeTolbar() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(allOf(withId(R.id.toolbar_cadastrachecklist),
                                withParent(withId(R.id.appbar)))),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Como usar"), isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Navegar para cima"),
                        withParent(withId(R.id.toolbar_comoUsar)),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(allOf(withId(R.id.toolbar_cadastrachecklist),
                                withParent(withId(R.id.appbar)))),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction appCompatCheckedTextView2 = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Compra de tags"), isDisplayed()));
        appCompatCheckedTextView2.perform(click());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withContentDescription("Navegar para cima"),
                        withParent(withId(R.id.toolbar_compraTag)),
                        isDisplayed()));
        appCompatImageButton4.perform(click());

        ViewInteraction appCompatImageButton5 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(allOf(withId(R.id.toolbar_cadastrachecklist),
                                withParent(withId(R.id.appbar)))),
                        isDisplayed()));
        appCompatImageButton5.perform(click());

        ViewInteraction appCompatCheckedTextView3 = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Sobre o TAGTRACKER"), isDisplayed()));
        appCompatCheckedTextView3.perform(click());

        ViewInteraction appCompatImageButton6 = onView(
                allOf(withContentDescription("Navegar para cima"),
                        withParent(withId(R.id.toolbar_sobre)),
                        isDisplayed()));
        appCompatImageButton6.perform(click());

        ViewInteraction appCompatImageButton7 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(allOf(withId(R.id.toolbar_cadastrachecklist),
                                withParent(withId(R.id.appbar)))),
                        isDisplayed()));
        appCompatImageButton7.perform(click());

        ViewInteraction appCompatCheckedTextView4 = onView(
                allOf(withId(R.id.design_menu_item_text), withText("TAGTRACKER Premium"), isDisplayed()));
        appCompatCheckedTextView4.perform(click());

        ViewInteraction appCompatImageButton8 = onView(
                allOf(withContentDescription("Navegar para cima"),
                        withParent(withId(R.id.toolbar_premium)),
                        isDisplayed()));
        appCompatImageButton8.perform(click());

    }

}