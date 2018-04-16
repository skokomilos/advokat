package com.example.berka.advokatormlite.activities.parnica;

/**
 * Created by berka on 16-Apr-18.
 */

public class AddParnicaModel implements AddParnicaContractMVP.Model{

    private AddParnicaRepository repository;

    public AddParnicaModel(AddParnicaRepository repository) {
        this.repository = repository;
    }
}
