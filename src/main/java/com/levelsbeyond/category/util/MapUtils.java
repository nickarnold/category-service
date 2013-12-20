package com.levelsbeyond.category.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {
	public static <C> C getValue(Map<String, ?> params, String paramName, C defaultVal) {
		if (params.containsKey(paramName)) {
			return (C) params.get(paramName);
		}
		else {
			return defaultVal;
		}
	}

	public static <K, V> Builder<K, V> hashMap(Class<K> keyClass, Class<V> valueClass) {
		return new HashMapBuilder<K, V>();
	}

	public abstract static class Builder<K, V> {
		protected abstract Map<K, V> getMap();

		public Builder<K, V> put(K key, V value) {
			getMap().put(key, value);

			return this;
		}

		public Map<K, V> build() {
			return getMap();
		}
	}

	private static class HashMapBuilder<K, V> extends Builder {
		private Map<K, V> map = new HashMap<>();

		@Override
		protected Map<K, V> getMap() {
			return map;
		}
	}
}