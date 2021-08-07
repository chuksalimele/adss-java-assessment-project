/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adsstest.func;

/**
 *
 * @author Chukwudi Alimele
 * @param <T>
 */
public interface Callback<T> {
    void call(T param);
}
