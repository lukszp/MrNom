package idig.za.net.conscalc;

/****************************
 * created by clive anthony *
 * www.101apps.co.za        *
 * **************************
 */

public class FuelRecord {
	private int rowId;
	private long date;
	private int odometer;
	private String litres;
	private String cost;

	public FuelRecord() {

	}

	public FuelRecord(int rowId, String registration, long date, int odometer,
			String litres, String cost) {
		this.rowId = rowId;
		this.date = date;
		this.odometer = odometer;
		this.litres = litres;
		this.cost = cost;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public int getRowId() {
		return rowId;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public long getDate() {
		return date;
	}

	public void setOdometer(int odometer) {
		this.odometer = odometer;
	}

	public int getOdometer() {
		return odometer;
	}

	public void setLitres(String litres) {
		this.litres = litres;
	}

	public String getLitres() {
		return litres;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getCost() {
		return cost;
	}

}