package com.binary_studio.uniq_in_sorted_stream;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public final class UniqueSortedStream {
    static Set<Long> uniqueSet = new HashSet<>();

    private UniqueSortedStream() {
    }

    public static <T> Stream<Row<T>> uniqueRowsSortedByPK(Stream<Row<T>> stream) {
        uniqueSet.clear();
        return stream == null ? Stream.of() : stream.map(Row::getPrimaryId)
                .filter(x -> uniqueSet.add(x))
                .map(Row::new);
    }

}
