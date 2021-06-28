package csedu.homeclick.androidhomeclick.database;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import csedu.homeclick.androidhomeclick.structure.FilterCriteria;

public class QueryBuilder {
    private static final String TAG = "QueryBuilder";

    private static final String AD_TABLE = "Advertisement";

    private Query filterQuery;
    private FilterCriteria filterCriteria;

    private final FirebaseFirestore firebaseFirestore;
    private final CollectionReference adCollectionReference;

    public QueryBuilder() {
        filterQuery = null;
        firebaseFirestore = FirebaseFirestore.getInstance();
        adCollectionReference = firebaseFirestore.collection(AD_TABLE);
    }

    public QueryBuilder(FilterCriteria filterCriteria) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        adCollectionReference = firebaseFirestore.collection(AD_TABLE);
        this.filterCriteria = filterCriteria;
    }

    public void setFilterCriteria(FilterCriteria filterCriteria) {
        this.filterCriteria = filterCriteria;
    }

    public Query buildQuery() {
        if(filterCriteria.getGasAvailability()) {
            if(filterQuery == null) {
                filterQuery = adCollectionReference.whereEqualTo("gasAvailability", true);
            } else {
                filterQuery = filterQuery.whereEqualTo("gasAvailability", true);
            }
        }

        if(filterCriteria.getAdType() != null) {
            String type = filterCriteria.getAdType();
            if(filterQuery == null) {
                filterQuery = adCollectionReference.whereEqualTo("adType", type);
            } else {
                filterQuery = filterQuery.whereEqualTo("adType", type);
            }
        }

        if(filterCriteria.getNumberOfBedrooms() != -1) {
            int bedrooms = filterCriteria.getNumberOfBedrooms();

            if(filterQuery == null) {
                filterQuery = adCollectionReference.whereEqualTo("numberOfBedrooms", bedrooms);
            } else {
                filterQuery = filterQuery.whereEqualTo("numberOfBedrooms", bedrooms);
            }
        }

        if(filterCriteria.getNumberOfBathrooms() != -1) {
            int bathrooms = filterCriteria.getNumberOfBathrooms();

            if(filterQuery == null) {
                filterQuery = adCollectionReference.whereEqualTo("numberOfBathrooms", bathrooms);
            } else {
                filterQuery = filterQuery.whereEqualTo("numberOfBathrooms", bathrooms);
            }
        }

        if(filterCriteria.getPaymentAmount() != -1) {
            int paymentAmount = filterCriteria.getPaymentAmount();

            if(filterQuery == null) {
                filterQuery = adCollectionReference.orderBy("paymentAmount", Query.Direction.ASCENDING).whereLessThanOrEqualTo("paymentAmount", paymentAmount);
            } else {
                filterQuery = filterQuery.orderBy("paymentAmount", Query.Direction.ASCENDING).whereLessThanOrEqualTo("paymentAmount", paymentAmount);
            }
        }


        return filterQuery;
    }
}

