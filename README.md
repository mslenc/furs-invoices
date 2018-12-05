# furs-invoices

This is a Java library for performing fiscal verification of invoices,
as required in Slovenia by FURS (Financial Administration of Republic of 
Slovenia).

## Installation

This library can be used in Maven or Gradle projects via jitpack.io. For example,
for Gradle you'd do this:

```
repositories {
    ...
    maven { url 'https://jitpack.io' }
}

dependencies {
    ...
    compile 'com.github.mslenc:furs-invoices:0.9.1'
}
```

See the [jitpack.io](https://jitpack.io/#mslenc/furs-invoices) site for other
build systems.

### Certificate conversion

Certificates from FURS are obtained in .p12 format. For various reasons this library 
uses PEM format instead. To convert from the former to the latter, use `openssl` with
a command like:

```bash
openssl pkcs12 -in 12345678-1.p12 -out 12345678-1.pem
```

### Initial set-up

The library currently uses BouncyCastle, so it has to be installed with

```java
Security.addProvider(new BouncyCastleProvider());
```

Then, you need to load the certificate one way or another (into a `byte[]`),
and obtain the password from somewhere. Then you can build a `FursConfig` object:

```java
String pathToCert = "some/path/12345678.pem";
byte[] pemBytes = Files.readAllBytes(Paths.get(pathToCert));
FursConfig config = FursConfig.createFromPem(pemBytes, "************".toCharArray(), FursEnv.TEST);
```

(for the production environment, use `FursEnv.PRODUCTION` instead)

Finally, you need to create a `FursClient`, providing it with a HTTP implementation.
A simple `java.net`-based one is included:.

```java
FursClient client = FursClient.create(config, new HttpClientJavaNet());
```

You could also use any other HTTP by simply implementing the one-method
interface `HttpClient`.

## Usage

To use the library, simply call the methods available on the client. 
For example, to register an individual electronic device "business 
premise":

```java
client.businessPremise(
    new BusinessPremise().
        setTaxNumber(12345678).
        setBusinessPremiseId("DEVICE1").
        setValidityDate(LocalDate.of(2018, 11, 15)).
        addSoftwareSupplier(new SoftwareSupplier().setTaxNumber(12597171)).
        setBpIdentifier(new BPIdentifier(MobilePremiseType.INDIVIDUAL_DEVICE))
);
// (returns nothing on success and throws an Exception on failure)
```

Or to confirm a simple invoice:

```java
Invoice invoice = new Invoice().
    setInvoiceAmount(12.20).
    setPaymentAmount(12.20).
    setInvoiceIdentifier(new InvoiceIdentifier("DEVICE1", "DEV1", "41294871")).
    setNumberingStructure(PER_REGISTER).
    setTaxNumber(12345678).
    setIssueDateTime(Instant.now()).
    setForeignOperator(true).
    addTaxPerSeller(
        new TaxPerSeller().addVat(
            new VAT(22.0, 10.0, 2.20)
        )
    )

UUID invoiceId = client.invoice(invoice);
```

## Asynchronous client

Since v0.9.1 there is also an asynchronous client version `FursClientAsync`. It is the same
in all respects, except it uses a `HttpClientAsync` to execute HTTP requests and returns
results as `CompletableFuture<>`. That way, it can be used in event-loop driven (non-blocking)
environments, like Vert.X or Netty.

Note that there is no default `HttpClientAsync` implementation, but it should be very easy
to write one that matches your environment. 

## Miscellaneous

* Most fields have validation on the values, so that you can't send
  invalid values at all (an `IllegalArgumentException` is thrown).
  
* The rate and amount fields accept both `BigDecimal` and `double` values.
  For `BigDecimal` the expectation is that the value provided is exact,
  meaning it can have at most 2 decimal digits. For `double` values, the
  expectation is waived and a `BigDecimal` is produced by rounding to two
  decimal digits. Note that using BigDecimal is recommended, so that no
  sub-cent amounts are lost anywhere.
  
* There is a number of date-time fields, and they accept both `LocalDateTime`
  and `Instant` values. The protocol seems to accept only `LocalDateTime` 
  values, and so instants are converted using the `Europe/Ljubljana` time
  zone.
  

## Licence

This software is licensed under the GNU AGPLv3, obtainable at
https://www.gnu.org/licenses/agpl-3.0.en.html

You can also obtain it under a commercial licence for a fee. Contact
the author for details.


##### Just a small note for future maintenance
  
To update the bundled FURS certificates, when they change again, follow
these steps:

1. Obtain certificates from "Digitalna Potrdila" section (or equivalent) at the
   [FURS page](https://edavki.durs.si/EdavkiPortal/OpenPortal/CommonPages/Opdynp/PageD.aspx?category=dpr_teh_spec)

2. Then convert the test certificates into PEM:

   ```bash
   openssl x509 -inform der -in sitest-ca.cer -out sitest-ca.pem
   openssl x509 -inform der -in TaxCATest.cer -out TaxCATest.pem
   openssl x509 -inform der -in test-sign.cer -out test-sign.pem
   mv test-tls.cer test-tls.pem
   ```

3. And also convert production certificates into PEM:

   ```bash
   mv DavPotRac.cer DavPotRac.pem
   openssl x509 -inform der -in blagajne.fu.gov.si.cer -out blagajne.fu.gov.si.pem
   openssl x509 -inform der -in sigov-ca2.xcert.crt -out sigov-ca2.xcert.pem
   openssl x509 -inform der -in si-trust-root.crt -out si-trust-root.pem
   ```

4. Move them to appropriate directories in resources.