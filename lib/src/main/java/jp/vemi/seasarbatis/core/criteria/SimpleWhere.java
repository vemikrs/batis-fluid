/*
 * Copyright(c) 2025 VEMI, All rights reserved.
 */
package jp.vemi.seasarbatis.core.criteria;

/**
 * シンプルなWHERE句を構築するクラス。
 * 
 * @deprecated このクラスは将来のバージョンで削除予定です。
 *             代わりに {@link jp.vemi.batisfluid.query.SimpleWhere} を使用してください。
 */
@Deprecated(since = "0.0.2", forRemoval = true)
public class SimpleWhere extends AbstractWhere<SimpleWhere> {
    public SimpleWhere() {
    }
}
