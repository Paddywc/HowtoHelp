package com.example.howtohelp.model.Data;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SocialIssueTest {

  @Test
  public void getHumanTitle() {
    final String HUMAN_TITLE = "Hello Human";
    SocialIssue socialIssue = new SocialIssue();
    socialIssue.setHumanTitle(HUMAN_TITLE);
    assertEquals("Test get get human title", HUMAN_TITLE, socialIssue.getHumanTitle());
  }
}
