// Generated by view binder compiler. Do not edit!
package com.s22010189.pil_pal.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.s22010189.pil_pal.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class PolicydialogBinding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final Button aagreeButton;

  @NonNull
  public final TextView ploicyText;

  @NonNull
  public final TextView policyFive;

  @NonNull
  public final TextView policyFour;

  @NonNull
  public final TextView policyOne;

  @NonNull
  public final TextView policyThree;

  @NonNull
  public final TextView policyTwo;

  private PolicydialogBinding(@NonNull ScrollView rootView, @NonNull Button aagreeButton,
      @NonNull TextView ploicyText, @NonNull TextView policyFive, @NonNull TextView policyFour,
      @NonNull TextView policyOne, @NonNull TextView policyThree, @NonNull TextView policyTwo) {
    this.rootView = rootView;
    this.aagreeButton = aagreeButton;
    this.ploicyText = ploicyText;
    this.policyFive = policyFive;
    this.policyFour = policyFour;
    this.policyOne = policyOne;
    this.policyThree = policyThree;
    this.policyTwo = policyTwo;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static PolicydialogBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static PolicydialogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.policydialog, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static PolicydialogBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.aagreeButton;
      Button aagreeButton = ViewBindings.findChildViewById(rootView, id);
      if (aagreeButton == null) {
        break missingId;
      }

      id = R.id.ploicyText;
      TextView ploicyText = ViewBindings.findChildViewById(rootView, id);
      if (ploicyText == null) {
        break missingId;
      }

      id = R.id.policyFive;
      TextView policyFive = ViewBindings.findChildViewById(rootView, id);
      if (policyFive == null) {
        break missingId;
      }

      id = R.id.policyFour;
      TextView policyFour = ViewBindings.findChildViewById(rootView, id);
      if (policyFour == null) {
        break missingId;
      }

      id = R.id.policyOne;
      TextView policyOne = ViewBindings.findChildViewById(rootView, id);
      if (policyOne == null) {
        break missingId;
      }

      id = R.id.policyThree;
      TextView policyThree = ViewBindings.findChildViewById(rootView, id);
      if (policyThree == null) {
        break missingId;
      }

      id = R.id.policyTwo;
      TextView policyTwo = ViewBindings.findChildViewById(rootView, id);
      if (policyTwo == null) {
        break missingId;
      }

      return new PolicydialogBinding((ScrollView) rootView, aagreeButton, ploicyText, policyFive,
          policyFour, policyOne, policyThree, policyTwo);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
