package com.howtohelp.howtohelp.ui;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.howtohelp.howtohelp.R;

/** Fragment for a card displayed in by the user guide adapter */
public class UserGuideCardFragment extends Fragment {

  /** Instantiates a new User guide card fragment. */
  public UserGuideCardFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_user_guide_card, container, false);
  }

  /** Displays guide clomplete message and allows user to close app or return to guide */
  private AlertDialog guideCompleteDialog() {
    return new AlertDialog.Builder(getContext())
        .setTitle(getResources().getString(R.string.guide_complete_title))
        .setMessage(getResources().getString(R.string.guide_complete_body))
        .setNegativeButton(
            "Return to Guide",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
              }
            })
        .setPositiveButton(
            "Close HowtoHelp",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                getActivity().finishAndRemoveTask();
              }
            })
        .setIcon(R.drawable.logo)
        .show();
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {

    if (getArguments() != null) {
      int position = getArguments().getInt("position");
      String positionAsString = "" + (position + 1);
      TextView overline = view.findViewById(R.id.user_guide_card_overline);
      Resources res = getResources();
      overline.setText(String.format(res.getString(R.string.step), positionAsString));

      ImageView imageView = view.findViewById(R.id.user_guide_card_header);
      TextView title = view.findViewById(R.id.user_guide_card_title);
      TextView body = view.findViewById(R.id.user_guide_card_body);

      // Sets the first cards value's to step 1 of the tutorial
      if (position == 0) {
        imageView.setImageResource(R.drawable.shareicon);
        title.setText(res.getString(R.string.step_one_title));
        body.setText(res.getString(R.string.step_one_body));
        // Sets the second cards value's to step 2 of the tutorial
      } else if (position == 1) {
        imageView.setImageResource(R.drawable.handsreachingsmaller);
        title.setText(res.getString(R.string.step_two_title));
        body.setText(res.getString(R.string.step_two_body));
        // Only 3 cards in ViewPager. Sets third card to step 3. Makes card button visible
      } else {
        imageView.setImageResource(R.drawable.powerwomantwo);
        title.setText(res.getString(R.string.step_three_title));
        body.setText(res.getString(R.string.step_three_body));
        Button button = view.findViewById(R.id.user_guide_card_button);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                guideCompleteDialog();
              }
            });
      }
    }
  }
}
