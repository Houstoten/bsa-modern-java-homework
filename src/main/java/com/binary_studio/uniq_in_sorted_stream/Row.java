package com.binary_studio.uniq_in_sorted_stream;

//YAGNI is here. Because of sorted)
public final class Row<RowData> {

	private static Long last;

	private final Long id;

	public Row(Long id) {
		this.id = id.equals(last) ? null : setLast(id);
	}

	private Long setLast(Long id) {
		last = id;
		return id;
	}

	static void removeLast() {
		last = null;
	}

	public Long getPrimaryId() {
		return this.id;
	}

}
