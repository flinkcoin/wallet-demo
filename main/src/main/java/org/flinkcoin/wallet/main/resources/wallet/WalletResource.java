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

import org.flinkcoin.wallet.main.api.ApiClient;
import org.flinkcoin.wallet.main.resources.ResourceBase;
import org.flinkcoin.wallet.main.resources.wallet.WalletDtl.WalletPaymentRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.flinkcoin.crypto.CryptoException;
import org.flinkcoin.data.proto.api.AccountServiceGrpc;
import org.flinkcoin.data.proto.api.Api;
import org.flinkcoin.data.proto.api.Api.TransactionReq;
import org.flinkcoin.data.proto.common.Common.Block;
import org.flinkcoin.helper.helpers.Base32Helper;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.flinkcoin.wallet.main.resources.wallet.WalletDtl.WalletTransaction;
import org.flinkcoin.data.proto.api.Api.PaymentTransactionReq;
import org.flinkcoin.data.proto.common.Common.PaymentRequest;

@Path("wallet")
public class WalletResource extends ResourceBase {

    @Context
    SecurityContext securityContext;

    private final ApiClient apiClient;

    @Inject
    public WalletResource(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @GET
    @Path("last-block/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response lastBlock(@PathParam("accountId") String accountId) throws SignatureException, InvalidKeyException, CryptoException, NoSuchAlgorithmException, JsonProcessingException {

        AccountServiceGrpc.AccountServiceBlockingStub accountServiceStub = apiClient.getAccountServiceStub();

        byte[] decode = Base32Helper.decode(accountId);

        Api.LastBlockReq accountReq = Api.LastBlockReq.newBuilder()
                .setAccountId(ByteString.copyFrom(decode))
                .build();

        Api.LastBlockRes lastBlock = accountServiceStub.lastBlock(accountReq);

        WalletDtl.WalletBlock walletBlock = new WalletDtl.WalletBlock(lastBlock.getBlock());

        return Response.ok(walletBlock).build();
    }

    @GET
    @Path("block/{blockHash}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBlock(@PathParam("blockHash") String blockhash) throws SignatureException, InvalidKeyException, CryptoException, NoSuchAlgorithmException, JsonProcessingException {

        AccountServiceGrpc.AccountServiceBlockingStub accountServiceStub = apiClient.getAccountServiceStub();

        byte[] decode = Base32Helper.decode(blockhash);

        Api.GetBlockReq blockReq = Api.GetBlockReq.newBuilder()
                .setBlockHash(ByteString.copyFrom(decode))
                .build();

        Api.GetBlockRes block = accountServiceStub.getBlock(blockReq);

        WalletDtl.WalletBlock walletBlock = new WalletDtl.WalletBlock(block.getBlock());

        return Response.ok(walletBlock).build();
    }

    @GET
    @Path("list-blocks/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listBlocks(@PathParam("accountId") String accountId) throws SignatureException, InvalidKeyException, CryptoException, NoSuchAlgorithmException, JsonProcessingException {

        AccountServiceGrpc.AccountServiceBlockingStub accountServiceStub = apiClient.getAccountServiceStub();

        byte[] decode = Base32Helper.decode(accountId);

        Api.ListBlockReq accountReq = Api.ListBlockReq.newBuilder()
                .setAccountId(ByteString.copyFrom(decode))
                .setNum(100)
                .build();

        Api.ListBlockRes listAccounts = accountServiceStub.listBlocks(accountReq);

        List<WalletDtl.WalletBlock> collect = listAccounts.getBlockList().stream()
                .map(x -> new WalletDtl.WalletBlock(x)).collect(Collectors.toList());

        return Response.ok(collect).build();
    }

    @GET
    @Path("list-unclaimed-blocks/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listUnclaimedBlocks(@PathParam("accountId") String accountId) throws SignatureException, InvalidKeyException, CryptoException, NoSuchAlgorithmException, JsonProcessingException {

        AccountServiceGrpc.AccountServiceBlockingStub accountServiceStub = apiClient.getAccountServiceStub();

        byte[] decode = Base32Helper.decode(accountId);

        Api.ListUnclaimedBlockReq accountReq = Api.ListUnclaimedBlockReq.newBuilder()
                .setAccountId(ByteString.copyFrom(decode))
                .setNum(100)
                .build();

        Api.ListUnclaimedBlockRes listAccounts = accountServiceStub.listUnclaimedBlocks(accountReq);

        List<WalletDtl.WalletBlock> collect = listAccounts.getBlockList().stream()
                .map(x -> new WalletDtl.WalletBlock(x)).collect(Collectors.toList());

        return Response.ok(collect).build();
    }

    @POST
    @Path("transaction")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transaction(WalletTransaction t) throws InvalidProtocolBufferException {
        AccountServiceGrpc.AccountServiceBlockingStub accountServiceStub = apiClient.getAccountServiceStub();

        TransactionReq.Builder transactionReqBuilder = TransactionReq.newBuilder();

        Block block = Block.parseFrom(Base32Helper.decode(t.encodedBlock));

        transactionReqBuilder.setBlock(block);

        Api.TransactionRes transactionRes = accountServiceStub.transaction(transactionReqBuilder.build());

        return Response.ok(transactionRes.getSuccess()).build();
    }

    /**
     * FIX THIS NO USER SIGNATURE!!!!
     *
     * @param t
     * @return
     * @throws InvalidProtocolBufferException
     */
    @POST
    @Path("payment-request")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response paymentRequest(WalletPaymentRequest t) throws InvalidProtocolBufferException {
        AccountServiceGrpc.AccountServiceBlockingStub accountServiceStub = apiClient.getAccountServiceStub();

        PaymentTransactionReq.Builder transactionReqBuilder = PaymentTransactionReq.newBuilder();

        PaymentRequest paymentRequest = PaymentRequest.parseFrom(Base32Helper.decode(t.encodedPaymentRequest));

        transactionReqBuilder.setPaymentRequest(paymentRequest);

        Api.PaymentTransactionRes paymentRequestRes = accountServiceStub.paymentRequest(transactionReqBuilder.build());

        return Response.ok(paymentRequestRes.getSuccess()).build();
    }


    @GET
    @Path("nftquery/{nftCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response queryNft(@PathParam("nftCode") String nftCode) throws SignatureException, InvalidKeyException, CryptoException, NoSuchAlgorithmException, JsonProcessingException {

        AccountServiceGrpc.AccountServiceBlockingStub accountServiceStub = apiClient.getAccountServiceStub();

        byte[] decode = Base32Helper.decode(nftCode);

        Api.QueryReq blockReq = Api.QueryReq.newBuilder()
                .setNftCode(ByteString.copyFrom(decode))
                .build();

        Api.QueryRes queryRes = accountServiceStub.queryNft(blockReq);

        WalletDtl.NFtMatch match=new WalletDtl.NFtMatch(queryRes);

        return Response.ok(match).build();
    }

    @POST
    @Path("vote-request")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response voteNFt(WalletDtl.NftVoteRequest t) throws InvalidProtocolBufferException {
        AccountServiceGrpc.AccountServiceBlockingStub accountServiceStub = apiClient.getAccountServiceStub();

        byte[] decode = Base32Helper.decode(t.nftCode);

        Api.VoteReq blockReq1 = Api.VoteReq.newBuilder()
                .setNftCode(ByteString.copyFrom(decode))
                .setReal(t.real)
                .build();

        Api.VoteRes voteRes = accountServiceStub.voteNft(blockReq1);

        Api.QueryReq blockReq2 = Api.QueryReq.newBuilder()
                .setNftCode(ByteString.copyFrom(decode))
                .build();

        Api.QueryRes queryRes = accountServiceStub.queryNft(blockReq2);

        return Response.ok(new WalletDtl.NftVoteResponse(queryRes.getNftVoteFake(),queryRes.getNftVoteReal())).build();
    }

}
