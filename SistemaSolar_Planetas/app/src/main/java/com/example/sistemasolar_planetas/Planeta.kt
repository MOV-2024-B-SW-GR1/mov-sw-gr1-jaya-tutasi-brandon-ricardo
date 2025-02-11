package com.example.sistemasolar_planetas

import android.os.Parcel
import android.os.Parcelable

class Planeta (
    var nombre: String,
    var diametro: Double,
    var periodoOrbital: Int,
    var esHabitable: Boolean,
    var temperaturaMedia: Float,
    var sistemaSolarId: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readFloat(),
        parcel.readInt() // Leer el sistemaSolarId
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeDouble(diametro)
        parcel.writeInt(periodoOrbital)
        parcel.writeByte(if (esHabitable) 1 else 0)
        parcel.writeFloat(temperaturaMedia)
        parcel.writeInt(sistemaSolarId) // Guardar el sistemaSolarId
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Planeta> {
        override fun createFromParcel(parcel: Parcel): Planeta {
            return Planeta(parcel)
        }

        override fun newArray(size: Int): Array<Planeta?> {
            return arrayOfNulls(size)
        }
    }
}