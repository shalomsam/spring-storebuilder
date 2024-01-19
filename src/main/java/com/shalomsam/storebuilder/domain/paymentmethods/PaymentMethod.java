package com.shalomsam.storebuilder.domain.paymentmethods;

import com.mongodb.lang.Nullable;
import com.shalomsam.storebuilder.domain.AuditMetadata;
import com.shalomsam.storebuilder.domain.user.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.*;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "paymentMethods")
public class PaymentMethod {
    @MongoId
    private ObjectId id;

    @Field("customerId")
    @DocumentReference
    private Customer customer;

    @Field("paymentMethodType")
    private String paymentMethodType;

    @Nullable
    private Card cardDetails;

    @Nullable
    @Field(targetType = FieldType.DECIMAL128)
    private BigInteger cashAmount;

    @Nullable
    private StripeDetails stripeDetails;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
