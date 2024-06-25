package com.s22010189.pil_pal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AddPharmaceuticals extends AppCompatActivity {

    private EditText drugsDose, quantityDrugs, descriptionsText;
    private Spinner unitDose, measureQuantity;
    private AutoCompleteTextView searchDrugPharmacist, genericsName;
    private Button addNutton;
    private static final String[] DRUGS = new String[] {
            "Aspirin", "Amitone", "Aero Spacer Plus", "Asthafen Tab", "Beclohale","Beclomate",
            "Beclovent","Belarmin Expectorant Syrup","Brilinta","Cetrine","Disprin","Flusal 250hfa Inhaler",
            "Flutihale", "Flutimate","Flutimate 50mcg Nasal Spray", "Flutivent","Foracort 100mcg Inhaler",
            "Halothane", "Montiget","Plavix","Foracort Dp Caps","Rupa"
    };
    private static final String[] GENERICS = new String[]{
            "acetaminophen", "acyclovir", "albuterol", "allopurinol", "alprazolam", "amantadine",
            "amiodarone", "amitriptyline", "amlodipine", "amoxicillin", "ampicillin", "anastrozole",
            "aripiprazole", "aspirin", "atenolol", "atorvastatin", "azathioprine", "azithromycin",
            "baclofen", "benazepril", "benzocaine", "benztropine", "bicalutamide", "bisoprolol",
            "brimonidine", "bromocriptine", "budesonide", "bumetanide", "buprenorphine", "bupropion",
            "buspirone", "butalbital", "cabergoline", "captopril", "carbamazepine", "carbidopa",
            "carvedilol", "cefadroxil", "cefdinir", "ceftriaxone", "cephalexin", "chlorpromazine",
            "cimetidine", "ciprofloxacin", "citalopram", "clarithromycin", "clindamycin",
            "clobetasol", "clomiphene", "clonazepam", "clonidine", "clopidogrel", "clotrimazole",
            "cyclobenzaprine", "cyclosporine", "dexamethasone", "dexlansoprazole", "dextromethorphan",
            "diazepam", "diclofenac", "digoxin", "diltiazem", "diphenhydramine", "divalproex",
            "doxycycline", "duloxetine", "enalapril", "enoxaparin", "escitalopram", "esomeprazole",
            "ethinyl estradiol", "ezetimibe", "famotidine", "fenofibrate", "fentanyl", "ferrous sulfate",
            "finasteride", "fluconazole", "fluoxetine", "fluticasone", "furosemide", "gabapentin",
            "gemfibrozil", "glimepiride", "glipizide", "glyburide", "guaifenesin", "haloperidol",
            "heparin", "hydralazine", "hydrochlorothiazide", "hydrocodone", "hydroxyzine",
            "ibuprofen", "imipramine", "indapamide", "indomethacin", "ipratropium", "isoniazid",
            "isotretinoin", "ketoconazole", "ketorolac", "lamotrigine", "lansoprazole", "latanoprost",
            "levofloxacin", "levothyroxine", "lisinopril", "lithium", "loratadine", "lorazepam",
            "losartan", "lovastatin", "meclizine", "meloxicam", "metformin", "methadone",
            "methimazole", "methocarbamol", "methotrexate", "methylphenidate", "metoclopramide",
            "metoprolol", "metronidazole", "mirtazapine", "montelukast", "morphine", "nabumetone",
            "naproxen", "nebivolol", "nicotine", "nitrofurantoin", "nitroglycerin", "norethindrone",
            "nortriptyline", "nystatin", "olanzapine", "omeprazole", "ondansetron", "oseltamivir",
            "oxycodone", "pantoprazole", "paroxetine", "penicillin", "phenazopyridine", "phenytoin",
            "pioglitazone", "polyethylene glycol", "potassium chloride", "pravastatin", "prednisone",
            "prochlorperazine", "promethazine", "propranolol", "quetiapine", "quinapril", "ranitidine",
            "risperidone", "rivastigmine", "rosuvastatin", "sertraline", "sildenafil", "simvastatin",
            "spironolactone", "sulfamethoxazole", "sumatriptan", "tadalafil", "tamsulosin",
            "temazepam", "terazosin", "testosterone", "thyroid", "tizanidine", "topiramate",
            "tramadol", "trazodone", "trihexyphenidyl", "trimethoprim", "valacyclovir", "valsartan",
            "venlafaxine", "verapamil", "warfarin", "zidovudine", "zolpidem"
    };
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DatabaseReference dbRef;
    private String storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpharmaceuticals);
        setStatusBarColor();

        searchDrugPharmacist = findViewById(R.id.pharmacistDrugSearch);
        drugsDose = findViewById(R.id.drugDose);
        quantityDrugs = findViewById(R.id.drugQuantity);
        genericsName = findViewById(R.id.genericName);
        descriptionsText = findViewById(R.id.descriptionText);
        unitDose = findViewById(R.id.doseUnit);
        measureQuantity = findViewById(R.id.quantityMeasure);
        addNutton = findViewById(R.id.addtoButton);
        //Authorising user
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //Configuring AutoComplete
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, DRUGS);
        searchDrugPharmacist.setAdapter(adapter);
        ArrayAdapter<String> adapterz = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, GENERICS);
        genericsName.setAdapter(adapterz);
        //configuring Spinners
        ArrayAdapter<CharSequence> adapterx = ArrayAdapter.createFromResource(this,
                R.array.dose_array, android.R.layout.simple_spinner_item);
        adapterx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitDose.setAdapter(adapterx);

        ArrayAdapter<CharSequence> adaptery = ArrayAdapter.createFromResource(this,
                R.array.quantity_array, android.R.layout.simple_spinner_item);
        adaptery.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        measureQuantity.setAdapter(adaptery);
        //retrieving Firebase data
        if (currentUser != null) {
            String userId = currentUser.getUid();
            dbRef = FirebaseDatabase.getInstance().getReference("Pharmacists").child(userId);

            dbRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        storeName = task.getResult().child("storename").getValue(String.class);
                    } else {
                        Toast.makeText(AddPharmaceuticals.this, "Failed to retrieve your data", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            addNutton.setOnClickListener(v -> addPharmaceuticalData());
        } else {
            Toast.makeText(this, "Something went wrong. Please contact pil-pal administrators", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    //handling user inputs
    private void addPharmaceuticalData() {
        String drugName = searchDrugPharmacist.getText().toString();
        String dose = drugsDose.getText().toString();
        String quantity = quantityDrugs.getText().toString();
        String genericName = genericsName.getText().toString();
        String description = descriptionsText.getText().toString();
        String doseUnitValue = unitDose.getSelectedItem().toString();
        String quantityMeasureValue = measureQuantity.getSelectedItem().toString();
        //Setting erros for null fields
        if (drugName.isEmpty()) {
            Toast.makeText(this, "Drug name is mandatory", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dose.isEmpty()) {
            Toast.makeText(this, "Dose is mandatory", Toast.LENGTH_SHORT).show();
            return;
        }

        if (quantity.isEmpty()) {
            Toast.makeText(this, "Quantity is mandatory", Toast.LENGTH_SHORT).show();
            return;
        }
        //storing user inputs in FireStore
        if (storeName != null) {
            CollectionReference storeCollection = db.collection(storeName);
            storeCollection.whereEqualTo("drugName", drugName).whereEqualTo("dose", dose).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        // Existing drug validation
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                Toast.makeText(AddPharmaceuticals.this, "Provided pharmaceutical is already in the database with the same dose. Please try to edit it or change the dose", Toast.LENGTH_SHORT).show();
                            } else {
                                Map<String, Object> pharmaceutical = new HashMap<>();
                                pharmaceutical.put("drugName", drugName);
                                pharmaceutical.put("dose", dose);
                                pharmaceutical.put("doseUnit", doseUnitValue);
                                pharmaceutical.put("quantity", quantity);
                                pharmaceutical.put("quantityMeasure", quantityMeasureValue);
                                pharmaceutical.put("genericName", genericName);
                                pharmaceutical.put("description", description);

                                storeCollection.add(pharmaceutical)
                                        .addOnSuccessListener(documentReference -> {
                                            updateRealtimeDatabase(drugName, quantity);
                                            Toast.makeText(AddPharmaceuticals.this, "Pharmaceutical added to the database", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e ->
                                                Toast.makeText(AddPharmaceuticals.this, "There was an error adding data. Please contact administrators.", Toast.LENGTH_SHORT).show());
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Store name not available", Toast.LENGTH_SHORT).show();
        }
    }

    // Updating the count of realtime. Purpose: Need the count to show in DrugDetails, failed to get count from Firestore
    private void updateRealtimeDatabase(String drugName, String quantity) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Pharmacists").child(userId);

            Map<String, Object> drugData = new HashMap<>();
            drugData.put(drugName, quantity);

            // adding a sub child node
            userRef.child("drugData").updateChildren(drugData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddPharmaceuticals.this, "Drug data updated in Realtime Database", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddPharmaceuticals.this, "Failed to update drug data in Realtime Database", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    //statusbar color
    private void setStatusBarColor() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int statusBarColor = ContextCompat.getColor(this, R.color.statusbar);
            window.setStatusBarColor(statusBarColor);

            //fixing all get white issue.
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (isColorDark(statusBarColor)) {
                    window.getDecorView().setSystemUiVisibility(0); // Light text
                } else {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // Dark text
                }
            }
        }
    }
    private boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }
}