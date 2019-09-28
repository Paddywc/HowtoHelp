package com.howtohelp.howtohelp.model.Data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.howtohelp.howtohelp.RxSchedulersOverrideRule;

import org.junit.Rule;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class TwoUsersCanQueryDbSimultaneously {

  @Rule public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

  @Rule public RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

  @Test
  public void testTwoUsersCanQueryDbSimultaneously() {

    final String CATEGORY = "Racism";
    final int MAX_RESULTS = 2;
    MicroVolunteeringRepository microVolunteeringRepositoryOne = new MicroVolunteeringRepository();
    MicroVolunteeringRepository microVolunteeringRepositoryTwo = new MicroVolunteeringRepository();
    microVolunteeringRepositoryOne.setParameters(CATEGORY, MAX_RESULTS);
    microVolunteeringRepositoryTwo.setParameters(CATEGORY, MAX_RESULTS);

    new Thread(
            new Runnable() {
              @Override
              public void run() {
                try {
                  microVolunteeringRepositoryOne.queryAllOpportunitiesMatchingParameters();
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            })
        .start();
    new Thread(
            new Runnable() {
              @Override
              public void run() {
                try {
                  microVolunteeringRepositoryTwo.queryAllOpportunitiesMatchingParameters();
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            })
        .start();

    try {
      microVolunteeringRepositoryTwo.queryAllOpportunitiesMatchingParameters();
    } catch (Exception e) {
      e.printStackTrace();
    }
    int loops = 0;
    boolean atLeaseOne = false;
    while (loops < 10 && !atLeaseOne) {
      try {
        Thread.sleep(1000);

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      atLeaseOne =
          microVolunteeringRepositoryOne.getRepository().getValue().size() > 0
              && microVolunteeringRepositoryTwo.getRepository().getValue().size() > 0;
      ++loops;
    }
    assertTrue(atLeaseOne);

    for (int index = 0;
        index < microVolunteeringRepositoryOne.getRepository().getValue().size();
        index++) {
      assertEquals(
          microVolunteeringRepositoryOne.getRepository().getValue().get(index).getDescription(),
          microVolunteeringRepositoryTwo.getRepository().getValue().get(index).getDescription());
    }
  }
}
