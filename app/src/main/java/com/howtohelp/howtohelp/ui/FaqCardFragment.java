package com.howtohelp.howtohelp.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.howtohelp.howtohelp.R;

/** Fragment for a card displayed in by the user guide adapter */
public class FaqCardFragment extends Fragment {

  /** Instantiates a new User guide card fragment. */
  public FaqCardFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_user_guide_card, container, false);
  }

  private void goToUrl(String urlAsString) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(urlAsString));
    startActivity(intent);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {

    // Vertically  align card to center
    ConstraintLayout outerCardLayout =
        view.findViewById(R.id.outer_user_guide_card_constraint_layout);
    outerCardLayout.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
    view.requestLayout();

    if (getArguments() != null) {
      int position = getArguments().getInt("position");
      Resources res = getResources();

      TextView overline = view.findViewById(R.id.user_guide_card_overline);
      overline.setText(res.getString(R.string.faq_overline));

      ImageView imageView = view.findViewById(R.id.user_guide_card_header);
      TextView title = view.findViewById(R.id.user_guide_card_title);
      TextView body = view.findViewById(R.id.user_guide_card_body);
      Button button = view.findViewById(R.id.user_guide_card_button);

      if (position == 0) {
        imageView.setImageResource(R.drawable.contact_dev);
        title.setText(res.getString(R.string.contact_dev_title));
        body.setText(res.getString(R.string.contact_dev_body));
        button.setText(res.getString(R.string.email_button_text));
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                goToUrl(res.getString(R.string.developer_email));
              }
            });
      } else if (position == 1) {
        imageView.setImageResource(R.drawable.world_cropped);
        title.setText(res.getString(R.string.issues_covered_title));
        body.setText(res.getString(R.string.issues_covered_body));
      } else if (position == 2) {
        imageView.setImageResource(R.drawable.politician);
        title.setText(res.getString(R.string.political_stance_title));
        body.setText(res.getString(R.string.political_stance_body));
      } else if (position == 3) {
        imageView.setImageResource(R.drawable.privacy);
        title.setText(res.getString(R.string.privacy_title));
        body.setText(res.getString(R.string.privacy_body));
        button.setText(res.getString(R.string.privacy_button));
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                goToUrl(res.getString(R.string.privacy_policy_url));
              }
            });
      } else if (position == 4) {
        imageView.setImageResource(R.drawable.source_code);
        title.setText(res.getString(R.string.source_code_title));
        body.setText(res.getString(R.string.source_code_body));
        button.setText(res.getString(R.string.source_code_button));
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                goToUrl(res.getString(R.string.source_code_url));
              }
            });
      } else {
        imageView.setImageResource(R.drawable.funding);
        title.setText(res.getString(R.string.funding_title));
        body.setText(res.getString(R.string.funding_body));
      }
    }
  }
}
