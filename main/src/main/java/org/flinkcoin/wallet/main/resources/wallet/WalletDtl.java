/*
 * Copyright © 2021 Flink Foundation (info@flinkcoin.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flinkcoin.wallet.main.resources.wallet;

import org.flinkcoin.data.proto.api.Api.QueryRes;
import org.flinkcoin.data.proto.common.Common.Block;
import org.flinkcoin.data.proto.common.Common.Block.BlockType;
import org.flinkcoin.helper.helpers.Base32Helper;

public class WalletDtl {

    public static class WalletBlock {

        public String accountId;
        public Long timestamp;
        public Long balance;
        public Long amount;
        public String hash;
        public String previousHash;
        public String sendAccountId;
        public String receiveBlockHash;
        public BlockType blockType;
        public String referenceCode;
        public String accountCode;
        public String nftCode;
        public boolean spotterVoteReal;

        public WalletBlock(Block block) {
            this.accountId = Base32Helper.encode(block.getBody().getAccountId().toByteArray());
            this.timestamp = block.getBody().getTimestamp();
            this.balance = block.getBody().getBalance();
            this.amount = block.getBody().getAmount();
            this.hash = Base32Helper.encode(block.getBlockHash().getHash().toByteArray());
            this.previousHash = Base32Helper.encode(block.getBody().getPreviousBlockHash().toByteArray());
            this.sendAccountId = Base32Helper.encode(block.getBody().getSendAccountId().toByteArray());
            this.blockType = block.getBody().getBlockType();
            this.sendAccountId = Base32Helper.encode(block.getBody().getSendAccountId().toByteArray());
            this.referenceCode = block.getBody().getReferenceCode().toStringUtf8();
            this.accountCode = block.getBody().getAccountCode().toStringUtf8();
            this.nftCode = Base32Helper.encode(block.getBody().getNftCode().toByteArray());
            this.receiveBlockHash = Base32Helper.encode(block.getBody().getReceiveBlockHash().toByteArray());
            this.spotterVoteReal = block.getBody().getSpotterVoteReal();

        }

    }

    public static class WalletTransaction {

        public String encodedBlock;
    }

    public static class WalletPaymentRequest {

        public String encodedPaymentRequest;
    }

    public static class NftVoteRequest {
        public String nftCode;
        public boolean real;
    }


    public static class NftVoteResponse {
        public int nftVoteFake;
        public int nftVoteReal;

        public NftVoteResponse(int nftVoteFake, int nftVoteReal) {
            this.nftVoteFake = nftVoteFake;
            this.nftVoteReal = nftVoteReal;
        }
    }

    public static class NFtMatch {
        public String accountId;
        public String accountCode;
        public boolean exists;
        public int nftVoteFake;
        public int nftVoteReal;
        public boolean nftVoteSpotter;

        public NFtMatch(QueryRes queryRes) {
            this.accountId = Base32Helper.encode(queryRes.getAccountId().toByteArray());
            this.accountCode = queryRes.getAccountCode().toStringUtf8();
            this.exists = queryRes.getExists();
            this.nftVoteFake = queryRes.getNftVoteFake();
            this.nftVoteReal = queryRes.getNftVoteReal();
            this.nftVoteSpotter= queryRes.getSpotterVoteReal();
        }
    }
}
