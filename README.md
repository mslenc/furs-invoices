

Obtain certificates from "Digitalna Potrdila" section at
https://edavki.durs.si/EdavkiPortal/OpenPortal/CommonPages/Opdynp/PageD.aspx?category=dpr_teh_spec

Then:

Convert test certificates into PEM:

```bash
openssl x509 -inform der -in sitest-ca.cer -out sitest-ca.pem
openssl x509 -inform der -in TaxCATest.cer -out TaxCATest.pem
openssl x509 -inform der -in test-sign.cer -out test-sign.pem
mv test-tls.cer test-tls.pem
```

Convert production certificates into PEM:

```bash
mv DavPotRac.cer DavPotRac.pem
openssl x509 -inform der -in blagajne.fu.gov.si.cer -out blagajne.fu.gov.si.pem
openssl x509 -inform der -in sigov-ca2.xcert.crt -out sigov-ca2.xcert.pem
openssl x509 -inform der -in si-trust-root.crt -out si-trust-root.pem
```

