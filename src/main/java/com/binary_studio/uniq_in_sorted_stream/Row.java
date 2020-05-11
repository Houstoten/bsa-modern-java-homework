package com.binary_studio.uniq_in_sorted_stream;

//YAGNI is here. Because of sorted)
public final class Row<RowData> {

	private static Long last;

	private final boolean uniq;

	private final Long id;

	public Row(Long id) {
		this.id = id;
		this.uniq = !id.equals(last) && setLast(id);
	}

	private boolean setLast(Long id) {
		last = id;
		return true;
	}

	static void removeLast() {
		last = null;
	}

	public Long getPrimaryId() {
		return this.id;
	}

	public boolean getUniq() {
		return this.uniq;
	}

}
