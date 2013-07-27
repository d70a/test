package ru.nsk.test.cabinet.core;

import java.io.Serializable;

/**
 *
 * @author me
 */
public interface Identifiable<ID extends Serializable> {

    ID getId();
}
