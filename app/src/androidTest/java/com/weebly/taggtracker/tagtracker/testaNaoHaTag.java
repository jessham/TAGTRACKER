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
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class testaNaoHaTag {

    /*
    testaNaoHaTag
        Tenta salvar uma checkList mas não há TAG
	          O aplicativo não funciona
	           O usuário cancela a operação de cadastrar tag
     */

    @Rule
    public ActivityTestRule<TelaInicialActivity> mActivityTestRule = new ActivityTestRule<>(TelaInicialActivity.class);

    @Test
    public void testaNaoHaTag() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.btnChecklists), isDisplayed()));
        relativeLayout.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.txtTitleChecklist), withContentDescription("Título da Checklist"), isDisplayed()));
        appCompatEditText.perform(replaceText("faculdade"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.txtTitleChecklist), withText("faculdade"), withContentDescription("Título da Checklist"), isDisplayed()));
        appCompatEditText2.perform(pressImeActionButton());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btnCancelarChecklist), withText("Cancelar"), isDisplayed()));
        appCompatButton.perform(click());

    }

}
