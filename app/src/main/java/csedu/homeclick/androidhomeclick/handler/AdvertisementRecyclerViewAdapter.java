package csedu.homeclick.androidhomeclick.handler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import csedu.homeclick.androidhomeclick.R;
import csedu.homeclick.androidhomeclick.structure.Advertisement;

public class AdvertisementRecyclerViewAdapter extends RecyclerView.Adapter<AdvertisementRecyclerViewAdapter.ViewHolder> {

    private List<Advertisement> advertisementArrayList = new ArrayList<>();

    public AdvertisementRecyclerViewAdapter() {
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_card, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.areaName.setText(advertisementArrayList.get(position).getAreaName());
        holder.adType.setText(advertisementArrayList.get(position).getAdType());
        holder.fullAddress.setText(advertisementArrayList.get(position).getFullAddress());
        holder.bedroom.setText(String.format(Locale.getDefault(), "%d", advertisementArrayList.get(position).getNumberOfBedrooms()));
        holder.bathroom.setText(String.format(Locale.getDefault(), "%d", advertisementArrayList.get(position).getNumberOfBathrooms()));

        Boolean gasAvail = advertisementArrayList.get(position).getGasAvailability();
        String gasString;
        if(gasAvail) gasString = "Available";
        else gasString = "Not Available";

        holder.gasAvailability.setText(gasString);
        holder.paymentAmount.setText(String.format(Locale.getDefault(), "%d", advertisementArrayList.get(position).getPaymentAmount()) + " BDT");
    }

    @Override
    public int getItemCount() {
        return advertisementArrayList.size();
    }

    public void setAdvertisementArrayList(List<Advertisement> advertisementArrayList) {
        this.advertisementArrayList = advertisementArrayList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView areaName, fullAddress, bedroom, bathroom, gasAvailability, paymentAmount, adType;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            bindWidgets(itemView);
        }

        private void bindWidgets(View view) {
            areaName = view.findViewById(R.id.card_area_name);
            fullAddress = view.findViewById(R.id.card_full_address);
            bedroom = view.findViewById(R.id.card_bedroom);
            bathroom = view.findViewById(R.id.card_bathroom);
            gasAvailability = view.findViewById(R.id.card_gas);
            paymentAmount = view.findViewById(R.id.card_payment);
            adType = view.findViewById(R.id.card_ad_type);
        }
    }
}
