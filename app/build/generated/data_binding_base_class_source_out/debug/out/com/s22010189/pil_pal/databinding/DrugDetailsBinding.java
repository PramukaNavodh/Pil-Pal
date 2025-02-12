// Generated by view binder compiler. Do not edit!
package com.s22010189.pil_pal.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.s22010189.pil_pal.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DrugDetailsBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout constraintLayout;

  @NonNull
  public final TextView estd;

  @NonNull
  public final ImageView imageView12;

  @NonNull
  public final TextView isAvl;

  @NonNull
  public final ConstraintLayout linearLayout;

  @NonNull
  public final LinearLayout linearSide;

  @NonNull
  public final TextView phaM;

  @NonNull
  public final TextView pharmCount;

  @NonNull
  public final TextView pharmDescription;

  @NonNull
  public final TextView pharmDescriptionB;

  @NonNull
  public final TextView pharmDescriptionC;

  @NonNull
  public final ImageView pharmImage;

  @NonNull
  public final TextView pharmName;

  @NonNull
  public final TextView pharmNameB;

  @NonNull
  public final TextView pharmPrice;

  @NonNull
  public final TextView pharmSideEffects;

  @NonNull
  public final ImageView saveStar;

  @NonNull
  public final ScrollView scrollView1;

  @NonNull
  public final ScrollView scrollView2;

  @NonNull
  public final Button seeavlblPharmacies;

  @NonNull
  public final Button shareDrug;

  @NonNull
  public final TextView textView22;

  @NonNull
  public final TextView textView23;

  @NonNull
  public final TextView textView25;

  private DrugDetailsBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout constraintLayout, @NonNull TextView estd,
      @NonNull ImageView imageView12, @NonNull TextView isAvl,
      @NonNull ConstraintLayout linearLayout, @NonNull LinearLayout linearSide,
      @NonNull TextView phaM, @NonNull TextView pharmCount, @NonNull TextView pharmDescription,
      @NonNull TextView pharmDescriptionB, @NonNull TextView pharmDescriptionC,
      @NonNull ImageView pharmImage, @NonNull TextView pharmName, @NonNull TextView pharmNameB,
      @NonNull TextView pharmPrice, @NonNull TextView pharmSideEffects, @NonNull ImageView saveStar,
      @NonNull ScrollView scrollView1, @NonNull ScrollView scrollView2,
      @NonNull Button seeavlblPharmacies, @NonNull Button shareDrug, @NonNull TextView textView22,
      @NonNull TextView textView23, @NonNull TextView textView25) {
    this.rootView = rootView;
    this.constraintLayout = constraintLayout;
    this.estd = estd;
    this.imageView12 = imageView12;
    this.isAvl = isAvl;
    this.linearLayout = linearLayout;
    this.linearSide = linearSide;
    this.phaM = phaM;
    this.pharmCount = pharmCount;
    this.pharmDescription = pharmDescription;
    this.pharmDescriptionB = pharmDescriptionB;
    this.pharmDescriptionC = pharmDescriptionC;
    this.pharmImage = pharmImage;
    this.pharmName = pharmName;
    this.pharmNameB = pharmNameB;
    this.pharmPrice = pharmPrice;
    this.pharmSideEffects = pharmSideEffects;
    this.saveStar = saveStar;
    this.scrollView1 = scrollView1;
    this.scrollView2 = scrollView2;
    this.seeavlblPharmacies = seeavlblPharmacies;
    this.shareDrug = shareDrug;
    this.textView22 = textView22;
    this.textView23 = textView23;
    this.textView25 = textView25;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DrugDetailsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DrugDetailsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.drug_details, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DrugDetailsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      ConstraintLayout constraintLayout = (ConstraintLayout) rootView;

      id = R.id.estd;
      TextView estd = ViewBindings.findChildViewById(rootView, id);
      if (estd == null) {
        break missingId;
      }

      id = R.id.imageView12;
      ImageView imageView12 = ViewBindings.findChildViewById(rootView, id);
      if (imageView12 == null) {
        break missingId;
      }

      id = R.id.isAvl;
      TextView isAvl = ViewBindings.findChildViewById(rootView, id);
      if (isAvl == null) {
        break missingId;
      }

      id = R.id.linearLayout;
      ConstraintLayout linearLayout = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout == null) {
        break missingId;
      }

      id = R.id.linearSide;
      LinearLayout linearSide = ViewBindings.findChildViewById(rootView, id);
      if (linearSide == null) {
        break missingId;
      }

      id = R.id.phaM;
      TextView phaM = ViewBindings.findChildViewById(rootView, id);
      if (phaM == null) {
        break missingId;
      }

      id = R.id.pharmCount;
      TextView pharmCount = ViewBindings.findChildViewById(rootView, id);
      if (pharmCount == null) {
        break missingId;
      }

      id = R.id.pharmDescription;
      TextView pharmDescription = ViewBindings.findChildViewById(rootView, id);
      if (pharmDescription == null) {
        break missingId;
      }

      id = R.id.pharmDescriptionB;
      TextView pharmDescriptionB = ViewBindings.findChildViewById(rootView, id);
      if (pharmDescriptionB == null) {
        break missingId;
      }

      id = R.id.pharmDescriptionC;
      TextView pharmDescriptionC = ViewBindings.findChildViewById(rootView, id);
      if (pharmDescriptionC == null) {
        break missingId;
      }

      id = R.id.pharmImage;
      ImageView pharmImage = ViewBindings.findChildViewById(rootView, id);
      if (pharmImage == null) {
        break missingId;
      }

      id = R.id.pharmName;
      TextView pharmName = ViewBindings.findChildViewById(rootView, id);
      if (pharmName == null) {
        break missingId;
      }

      id = R.id.pharmNameB;
      TextView pharmNameB = ViewBindings.findChildViewById(rootView, id);
      if (pharmNameB == null) {
        break missingId;
      }

      id = R.id.pharmPrice;
      TextView pharmPrice = ViewBindings.findChildViewById(rootView, id);
      if (pharmPrice == null) {
        break missingId;
      }

      id = R.id.pharmSideEffects;
      TextView pharmSideEffects = ViewBindings.findChildViewById(rootView, id);
      if (pharmSideEffects == null) {
        break missingId;
      }

      id = R.id.saveStar;
      ImageView saveStar = ViewBindings.findChildViewById(rootView, id);
      if (saveStar == null) {
        break missingId;
      }

      id = R.id.scrollView1;
      ScrollView scrollView1 = ViewBindings.findChildViewById(rootView, id);
      if (scrollView1 == null) {
        break missingId;
      }

      id = R.id.scrollView2;
      ScrollView scrollView2 = ViewBindings.findChildViewById(rootView, id);
      if (scrollView2 == null) {
        break missingId;
      }

      id = R.id.seeavlblPharmacies;
      Button seeavlblPharmacies = ViewBindings.findChildViewById(rootView, id);
      if (seeavlblPharmacies == null) {
        break missingId;
      }

      id = R.id.shareDrug;
      Button shareDrug = ViewBindings.findChildViewById(rootView, id);
      if (shareDrug == null) {
        break missingId;
      }

      id = R.id.textView22;
      TextView textView22 = ViewBindings.findChildViewById(rootView, id);
      if (textView22 == null) {
        break missingId;
      }

      id = R.id.textView23;
      TextView textView23 = ViewBindings.findChildViewById(rootView, id);
      if (textView23 == null) {
        break missingId;
      }

      id = R.id.textView25;
      TextView textView25 = ViewBindings.findChildViewById(rootView, id);
      if (textView25 == null) {
        break missingId;
      }

      return new DrugDetailsBinding((ConstraintLayout) rootView, constraintLayout, estd,
          imageView12, isAvl, linearLayout, linearSide, phaM, pharmCount, pharmDescription,
          pharmDescriptionB, pharmDescriptionC, pharmImage, pharmName, pharmNameB, pharmPrice,
          pharmSideEffects, saveStar, scrollView1, scrollView2, seeavlblPharmacies, shareDrug,
          textView22, textView23, textView25);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
