// Generated by view binder compiler. Do not edit!
package com.s22010189.pil_pal.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.s22010189.pil_pal.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class PharmaceuticalItemBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button deleteButton;

  @NonNull
  public final TextView doseTextView;

  @NonNull
  public final Button editButton;

  @NonNull
  public final TextView nameTextView;

  @NonNull
  public final TextView quantityTextView;

  private PharmaceuticalItemBinding(@NonNull LinearLayout rootView, @NonNull Button deleteButton,
      @NonNull TextView doseTextView, @NonNull Button editButton, @NonNull TextView nameTextView,
      @NonNull TextView quantityTextView) {
    this.rootView = rootView;
    this.deleteButton = deleteButton;
    this.doseTextView = doseTextView;
    this.editButton = editButton;
    this.nameTextView = nameTextView;
    this.quantityTextView = quantityTextView;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static PharmaceuticalItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static PharmaceuticalItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.pharmaceutical_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static PharmaceuticalItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.deleteButton;
      Button deleteButton = ViewBindings.findChildViewById(rootView, id);
      if (deleteButton == null) {
        break missingId;
      }

      id = R.id.doseTextView;
      TextView doseTextView = ViewBindings.findChildViewById(rootView, id);
      if (doseTextView == null) {
        break missingId;
      }

      id = R.id.editButton;
      Button editButton = ViewBindings.findChildViewById(rootView, id);
      if (editButton == null) {
        break missingId;
      }

      id = R.id.nameTextView;
      TextView nameTextView = ViewBindings.findChildViewById(rootView, id);
      if (nameTextView == null) {
        break missingId;
      }

      id = R.id.quantityTextView;
      TextView quantityTextView = ViewBindings.findChildViewById(rootView, id);
      if (quantityTextView == null) {
        break missingId;
      }

      return new PharmaceuticalItemBinding((LinearLayout) rootView, deleteButton, doseTextView,
          editButton, nameTextView, quantityTextView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
