package com.example.fronteravictor_enterthecar

import android.os.Parcel
import android.os.Parcelable

data class Pago (val tipoTarjeta: String, val numeroTarjeta: String, val fechaCaducidad: String) : Parcelable {

    companion object CREATOR: Parcelable.Creator<Pago> {
        override fun createFromParcel(`in`: Parcel): Pago {
            return Pago(`in`)

        }

        override fun newArray(size: Int): Array<Pago?> {
            return arrayOfNulls(size)
        }
    }

    constructor(`in`: Parcel) : this(`in`.readString()!!, `in`.readString()!!, `in`.readString()!!)

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel?, flag: Int) {
        out?.writeString(tipoTarjeta)
        out?.writeString(numeroTarjeta)
        out?.writeString(fechaCaducidad)
    }
}
