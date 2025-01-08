package com.example.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "Signatures")
public class Signature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "detail_id", nullable = false)
    private Long detailId;

    @Column(name = "cna_id", nullable = false)
    private Long cnaId;

    @Column(name = "signer_name", nullable = false)
    private String signerName;

    @Column(name = "signature_data", nullable = false)
    private String signatureData;

    @Column(name = "date_signed", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSigned;

    @Column(name = "context", nullable = false)
    private String context;

    public Signature() {
    }

    public Signature(Long detailId, Long cnaId, String signerName, String signatureData, Date dateSigned, String context) {
        this.detailId = detailId;
        this.cnaId = cnaId;
        this.signerName = signerName;
        this.signatureData = signatureData;
        this.dateSigned = dateSigned;
        this.context = context;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getCnaId() {
        return cnaId;
    }

    public void setCnaId(Long cnaId) {
        this.cnaId = cnaId;
    }

    public String getSignerName() {
        return signerName;
    }

    public void setSignerName(String signerName) {
        this.signerName = signerName;
    }

    public String getSignatureData() {
        return signatureData;
    }

    public void setSignatureData(String signatureData) {
        this.signatureData = signatureData;
    }

    public Date getDateSigned() {
        return dateSigned;
    }

    public void setDateSigned(Date dateSigned) {
        this.dateSigned = dateSigned;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Signature signature = (Signature) o;
        return Objects.equals(id, signature.id) &&
                Objects.equals(detailId, signature.detailId) &&
                Objects.equals(cnaId, signature.cnaId) &&
                Objects.equals(signerName, signature.signerName) &&
                Objects.equals(signatureData, signature.signatureData) &&
                Objects.equals(dateSigned, signature.dateSigned) &&
                Objects.equals(context, signature.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, detailId, cnaId, signerName, signatureData, dateSigned, context);
    }

    @Override
    public String toString() {
        return "Signature{" +
                "id=" + id +
                ", detailId=" + detailId +
                ", cnaId=" + cnaId +
                ", signerName='" + signerName + '\'' +
                ", signatureData='" + signatureData + '\'' +
                ", dateSigned=" + dateSigned +
                ", context='" + context + '\'' +
                '}';
    }
}
