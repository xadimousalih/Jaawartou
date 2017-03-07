package com.tab28.jaawartou;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
/**
 * @author xadimouSALIH
 * 
 */

public class CustomListViewAdapter extends ArrayAdapter<RowItem> {

	Context context;

	public CustomListViewAdapter(Context context, int resourceId,
			List<RowItem> items) {
		super(context, resourceId, items);
		this.context = context;
	}

	private class ViewHolder {
		TextView txtFran;
		TextView txtArab;
		TextView txtTran;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		RowItem rowItem = getItem(position);
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.txtArab = (TextView) convertView.findViewById(R.id.arab);
			holder.txtFran = (TextView) convertView.findViewById(R.id.fran);
			holder.txtTran = (TextView) convertView.findViewById(R.id.tran);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
			holder.txtArab.setText(rowItem.getArab());
			holder.txtFran.setText(rowItem.getFran());
			holder.txtTran.setText(rowItem.getTran());
		return convertView;
	}
}