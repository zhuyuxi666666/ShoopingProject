package com.china.lhf.app.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.china.lhf.app.R;
import com.china.lhf.app.entity.Address;

import java.util.List;


public class AddressAdapter extends SimpleAdapter<Address> {

    private AddressListener addrListener;

    public AddressAdapter(Context context, List<Address> datas, AddressListener addrsListener) {
        super(datas,context, R.layout.item_address);
        this.addrListener = addrsListener;
    }


    @Override
    protected void convert(BaseViewHoder viewHolder, final Address item) {
        viewHolder.getTextView(R.id.consignee_tv).setText(item.getConsignee());
        viewHolder.getTextView(R.id.people_phone_tv).setText(replacePhoneNum(item.getPhone()));
        viewHolder.getTextView(R.id.addr_name_tv).setText(item.getAddr());


        final CheckBox chooseAddrCB = viewHolder.getCheckBox(R.id.choose_addr_cb);
        final boolean isDefault = item.getIsDefault();
        chooseAddrCB.setChecked(isDefault);
        final TextView setAddrTV = viewHolder.getTextView(R.id.set_addr_tv);
        final TextView delAddrTV = viewHolder.getTextView(R.id.del_address_tv);

        if (isDefault) {
            setAddrTV.setText("默认地址");
            chooseAddrCB.setClickable(false);
            delAddrTV.setVisibility(View.INVISIBLE);

        } else {
            setAddrTV.setText("设为默认");
            chooseAddrCB.setClickable(true);
            chooseAddrCB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addrListener != null) {
                        item.setIsDefault(true);
                        addrListener.setDefault(item);
                    }
                }
            });

            delAddrTV.setVisibility(View.VISIBLE);
            delAddrTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addrListener.deleteAddress(item.getId());
                }
            });
        }
        viewHolder.getTextView(R.id.edit_address_tv)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addrListener.editAddress(item);
            }
        });
    }

    public String replacePhoneNum(String phone) {
        return phone.substring(0, phone.length() - (phone.substring(3)).length()) + "****"
                + phone.substring(7);
    }

    public interface AddressListener {
        public void setDefault(Address address);

        public void deleteAddress(Long id);

        public void editAddress(Address address);
    }

}
