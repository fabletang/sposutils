-printusage shrinking.outpu
-dontobfuscate
-dontoptimize
-keepattributes *Annotation*,EnclosingMethod
-dontwarn
# -keep public class com.pax.spos.utils.ByteStringHex
-keep public class * {
   # public protected *;
public static void main(java.lang.String[]);
}

