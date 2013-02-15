package idig.za.net.conscalc;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class FuelRecordList extends ArrayList<FuelRecord> implements Parcelable {

	/****************************
	 * created by clive anthony *
	 * www.101apps.co.za        *
	 * **************************
	 */
	
	private static final long serialVersionUID = 1L;

	public FuelRecordList() {

	}

	public FuelRecordList(Parcel in) {
		readFromParcel(in);
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public FuelRecordList createFromParcel(Parcel in) {
			return new FuelRecordList(in);
		}

		@Override
		public Object[] newArray(int size) {
			return null;
		}
	};

	private void readFromParcel(Parcel in) {
		this.clear();

		int size = in.readInt();

		for (int i = 0; i < size; i++) {
			FuelRecord fR = new FuelRecord();
			fR.setRowId(in.readInt());
			fR.setDate(in.readLong());
			fR.setOdometer(in.readInt());
			fR.setLitres(in.readString());
			fR.setCost(in.readString());
			this.add(fR);
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		int size = this.size();
		dest.writeInt(size);
		for (int i = 0; i < size; i++) {
			FuelRecord fR = this.get(i);
			dest.writeInt(fR.getRowId());
			dest.writeLong(fR.getDate());
			dest.writeInt(fR.getOdometer());
			dest.writeString(fR.getLitres());
			dest.writeString(fR.getCost());
		}
	}

}
