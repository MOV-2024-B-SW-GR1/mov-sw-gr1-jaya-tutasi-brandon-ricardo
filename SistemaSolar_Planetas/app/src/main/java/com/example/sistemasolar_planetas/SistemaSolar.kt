package com.example.sistemasolar_planetas

import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SistemaSolar (
    var id: Int,  // 🔹 Agregar ID
    var nombre: String?,
    var fechaDescubrimiento: LocalDate,
    var masDeUnSol: Boolean,
    var numeroDePlanetas: Int,
    var distanciaAlCentro: Double,
    val latitud: Double,  // 🔹 Nueva propiedad
    val longitud: Double  // 🔹 Nueva propiedad
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),  // 🔹 Leer ID
        parcel.readString(),
        LocalDate.parse(parcel.readString(), DateTimeFormatter.ISO_DATE),  // 🔹 Leer fecha correctamente
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)  // 🔹 Guardar ID
        parcel.writeString(nombre)
        parcel.writeString(fechaDescubrimiento.toString())  // 🔹 Guardar fecha correctamente
        parcel.writeByte(if (masDeUnSol) 1 else 0)
        parcel.writeInt(numeroDePlanetas)
        parcel.writeDouble(distanciaAlCentro)
        parcel.writeDouble(latitud)  // 🔹 Guardamos latitud
        parcel.writeDouble(longitud) // 🔹 Guardamos longitud
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SistemaSolar> {
        override fun createFromParcel(parcel: Parcel): SistemaSolar {
            return SistemaSolar(parcel)
        }

        override fun newArray(size: Int): Array<SistemaSolar?> {
            return arrayOfNulls(size)
        }
    }
}