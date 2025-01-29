// Generated by view binder compiler. Do not edit!
package com.s22010189.pil_pal.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

public final class UserSignupBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Spinner countryCodeU;

  @NonNull
  public final Button directPharmacist;

  @NonNull
  public final EditText emailAddress;

  @NonNull
  public final EditText firstName;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final EditText lastName;

  @NonNull
  public final LinearLayout linearLayout;

  @NonNull
  public final EditText mobileNumber;

  @NonNull
  public final EditText passWord;

  @NonNull
  public final Button signUp;

  @NonNull
  public final Button signiDirec;

  @NonNull
  public final TextView textView;

  @NonNull
  public final TextView textView1;

  @NonNull
  public final TextView textView26;

  @NonNull
  public final TextView textView3;

  @NonNull
  public final TextView textView32;

  @NonNull
  public final TextView textView4;

  @NonNull
  public final TextView textView5;

  @NonNull
  public final TextView textView6;

  @NonNull
  public final TextView textView7;

  @NonNull
  public final Button user;

  @NonNull
  public final EditText userRePassword;

  private UserSignupBinding(@NonNull ConstraintLayout rootView, @NonNull Spinner countryCodeU,
      @NonNull Button directPharmacist, @NonNull EditText emailAddress, @NonNull EditText firstName,
      @NonNull ImageView imageView, @NonNull EditText lastName, @NonNull LinearLayout linearLayout,
      @NonNull EditText mobileNumber, @NonNull EditText passWord, @NonNull Button signUp,
      @NonNull Button signiDirec, @NonNull TextView textView, @NonNull TextView textView1,
      @NonNull TextView textView26, @NonNull TextView textView3, @NonNull TextView textView32,
      @NonNull TextView textView4, @NonNull TextView textView5, @NonNull TextView textView6,
      @NonNull TextView textView7, @NonNull Button user, @NonNull EditText userRePassword) {
    this.rootView = rootView;
    this.countryCodeU = countryCodeU;
    this.directPharmacist = directPharmacist;
    this.emailAddress = emailAddress;
    this.firstName = firstName;
    this.imageView = imageView;
    this.lastName = lastName;
    this.linearLayout = linearLayout;
    this.mobileNumber = mobileNumber;
    this.passWord = passWord;
    this.signUp = signUp;
    this.signiDirec = signiDirec;
    this.textView = textView;
    this.textView1 = textView1;
    this.textView26 = textView26;
    this.textView3 = textView3;
    this.textView32 = textView32;
    this.textView4 = textView4;
    this.textView5 = textView5;
    this.textView6 = textView6;
    this.textView7 = textView7;
    this.user = user;
    this.userRePassword = userRePassword;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static UserSignupBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static UserSignupBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.user_signup, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static UserSignupBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.countryCodeU;
      Spinner countryCodeU = ViewBindings.findChildViewById(rootView, id);
      if (countryCodeU == null) {
        break missingId;
      }

      id = R.id.directPharmacist;
      Button directPharmacist = ViewBindings.findChildViewById(rootView, id);
      if (directPharmacist == null) {
        break missingId;
      }

      id = R.id.emailAddress;
      EditText emailAddress = ViewBindings.findChildViewById(rootView, id);
      if (emailAddress == null) {
        break missingId;
      }

      id = R.id.firstName;
      EditText firstName = ViewBindings.findChildViewById(rootView, id);
      if (firstName == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.lastName;
      EditText lastName = ViewBindings.findChildViewById(rootView, id);
      if (lastName == null) {
        break missingId;
      }

      id = R.id.linearLayout;
      LinearLayout linearLayout = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout == null) {
        break missingId;
      }

      id = R.id.mobileNumber;
      EditText mobileNumber = ViewBindings.findChildViewById(rootView, id);
      if (mobileNumber == null) {
        break missingId;
      }

      id = R.id.passWord;
      EditText passWord = ViewBindings.findChildViewById(rootView, id);
      if (passWord == null) {
        break missingId;
      }

      id = R.id.signUp;
      Button signUp = ViewBindings.findChildViewById(rootView, id);
      if (signUp == null) {
        break missingId;
      }

      id = R.id.signiDirec;
      Button signiDirec = ViewBindings.findChildViewById(rootView, id);
      if (signiDirec == null) {
        break missingId;
      }

      id = R.id.textView;
      TextView textView = ViewBindings.findChildViewById(rootView, id);
      if (textView == null) {
        break missingId;
      }

      id = R.id.textView1;
      TextView textView1 = ViewBindings.findChildViewById(rootView, id);
      if (textView1 == null) {
        break missingId;
      }

      id = R.id.textView26;
      TextView textView26 = ViewBindings.findChildViewById(rootView, id);
      if (textView26 == null) {
        break missingId;
      }

      id = R.id.textView3;
      TextView textView3 = ViewBindings.findChildViewById(rootView, id);
      if (textView3 == null) {
        break missingId;
      }

      id = R.id.textView32;
      TextView textView32 = ViewBindings.findChildViewById(rootView, id);
      if (textView32 == null) {
        break missingId;
      }

      id = R.id.textView4;
      TextView textView4 = ViewBindings.findChildViewById(rootView, id);
      if (textView4 == null) {
        break missingId;
      }

      id = R.id.textView5;
      TextView textView5 = ViewBindings.findChildViewById(rootView, id);
      if (textView5 == null) {
        break missingId;
      }

      id = R.id.textView6;
      TextView textView6 = ViewBindings.findChildViewById(rootView, id);
      if (textView6 == null) {
        break missingId;
      }

      id = R.id.textView7;
      TextView textView7 = ViewBindings.findChildViewById(rootView, id);
      if (textView7 == null) {
        break missingId;
      }

      id = R.id.user;
      Button user = ViewBindings.findChildViewById(rootView, id);
      if (user == null) {
        break missingId;
      }

      id = R.id.userRePassword;
      EditText userRePassword = ViewBindings.findChildViewById(rootView, id);
      if (userRePassword == null) {
        break missingId;
      }

      return new UserSignupBinding((ConstraintLayout) rootView, countryCodeU, directPharmacist,
          emailAddress, firstName, imageView, lastName, linearLayout, mobileNumber, passWord,
          signUp, signiDirec, textView, textView1, textView26, textView3, textView32, textView4,
          textView5, textView6, textView7, user, userRePassword);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
