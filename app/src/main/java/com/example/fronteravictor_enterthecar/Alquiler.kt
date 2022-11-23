package com.example.fronteravictor_enterthecar

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.Menu

data class Alquiler(
    val nombre: String?, val apellido: String?, val vehiculo: String?,
    val combustible: String?, val gps: Boolean, val dias: String?,
    val precioTotal: String?) : Parcelable {

    companion object CREATOR: Parcelable.Creator<Alquiler> {
        override fun createFromParcel(`in`: Parcel): Alquiler {
            return Alquiler(`in`)
        }

        override fun newArray(size: Int): Array<Alquiler?> {
            return arrayOfNulls(size)
        }

    }

    constructor(`in`: Parcel) : this(`in`.readString(), `in`.readString(), `in`.readString(),
        `in`.readString(), `in`.readInt() !=0, `in`.readString(), `in`.readString())

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel?, flag: Int) {
        out?.writeString(nombre)
        out?.writeString(apellido)
        out?.writeString(vehiculo)
        out?.writeString(combustible)
        out?.writeInt(if(gps) 1 else 0)
        out?.writeString(dias)
        out?.writeString(precioTotal)

    }


}