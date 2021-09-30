package org.flinkcoin.wallet.main.message;

/*-
 * #%L
 * Wallet - Main
 * %%
 * Copyright (C) 2021 Flink Foundation
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.flick.data.proto.api.Api.InfoRes;
import com.flick.data.proto.api.Api.InfoRes.InfoType;
import com.flick.helper.helpers.Base32Helper;

public class MessageDtl {

    public static class InfoResponse {

        public InfoType infoType;
        public String accountId;

        public BlockConfirm blockConfirm;
        public PaymentReceived paymentReceived;
        public PaymentRequest paymentRequest;

        public InfoResponse() {
        }

        public InfoResponse(InfoRes infoRes) {

            this.infoType = infoRes.getInfoType();
            this.accountId = Base32Helper.encode(infoRes.getAccountId().toByteArray());

            switch (infoType) {

                case BLOCK_CONFIRM:
                    this.blockConfirm = new BlockConfirm(Base32Helper.encode(infoRes.getBlockConfirm().getBlockHash().toByteArray()));
                    break;
                case PAYMENT_RECEIVED:
                    this.paymentReceived = new PaymentReceived(Base32Helper.encode(infoRes.getPaymentReceived().getBlockHash().toByteArray()));
                    break;
                case PAYMENT_REQUEST:
                    this.paymentRequest = new PaymentRequest(Base32Helper.encode(infoRes.getPaymentRequest().getFromAccountId().toByteArray()),
                            infoRes.getPaymentRequest().getAmount(), Base32Helper.encode(infoRes.getPaymentRequest().getReferenceCode().toByteArray()));
                    break;

            }

        }
    }

    public static class BlockConfirm {

        public String blockHash;

        public BlockConfirm() {
        }

        public BlockConfirm(String blockHash) {
            this.blockHash = blockHash;
        }

    }

    public static class PaymentReceived {

        public String blockHash;

        public PaymentReceived() {
        }

        public PaymentReceived(String blockHash) {
            this.blockHash = blockHash;
        }

    }

    public static class PaymentRequest {

        public String accountId;
        public long amount;
        public String referenceCode;

        public PaymentRequest() {
        }

        public PaymentRequest(String accountId, long amount, String referenceCode) {
            this.accountId = accountId;
            this.amount = amount;
            this.referenceCode = referenceCode;
        }

    }
}
