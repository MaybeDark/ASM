package org;

import org.other.Pool;

/**
 * 可加载到常量池的
 */
public interface Loadable<T extends Pool> {
    short load(T pool);
}
