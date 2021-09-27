package com.binarybazaar.wallet.main.resources;

import com.binarybazaar.wallet.main.api.ApiClient;
import com.flick.crypto.CryptoException;
import com.flick.crypto.HashHelper;
import com.flick.crypto.KeyGenerator;
import com.flick.crypto.KeyPair;
import com.flick.data.proto.api.AccountServiceGrpc;
import com.flick.data.proto.api.Api;
import com.flick.data.proto.common.Common.Block;
import com.flick.helper.helpers.Base32Helper;
import com.flick.helper.helpers.DateHelper;
import com.flick.helper.helpers.RandomHelper;
import com.flick.helper.helpers.UUIDHelper;
import com.google.protobuf.ByteString;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.time.ZonedDateTime;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveSpec;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import javax.ws.rs.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/macgyver")
public class MacgyverResource {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MacgyverResource.class);
    
    private static final ByteString GENESIS_ACCOUNT_ID = ByteString.copyFrom(Base32Helper.decode("RA2RBX6FTNGDLLQJ4NPQSFBFLQ"));
    private static final ByteString GENESIS_SEED = ByteString.copyFrom(Base32Helper.decode("FBKXIWYQVC5NGEDILX67KHKZGHQAUUEIQF7J7XS7E4VJWOOFHZG6JFSFQH7XUYPSIXPF7YV2MV52RS3TD7VYNEYP3OR3XSOMPPNI77I"));
    
    public static final long MAX_SUPPLY = 9000000000000000000L;
    
    @Context
    SecurityContext securityContext;
    
    private final ApiClient apiClient;
    
    @Inject
    public MacgyverResource(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

//    @GET
//    @Path("test-transaction")
//    public Response testTransaction() throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, CryptoException {
//        AccountServiceGrpc.AccountServiceBlockingStub accountServiceStub = apiClient.getAccountServiceStub();
//
//        Api.TransactionReq.Builder transactionReqBuilder = Api.TransactionReq.newBuilder();
//
//        byte[] toAccountId = Base32Helper.decode("QBOYRZ2OC5EBHKECZAYJF2SVEU");
//        byte[] previousBlockHash = Base32Helper.decode("CKUZ2BMWDDCSHIRLN7N4QXIC337MIFGA3EY5SDB7XXD2VPAS4ECYNCSZ6CUHIHNAYFZNNMXO4WDQCEBNPKHWLDXDLULNTS4OAAVNOHA");
//
//        KeyPair keyPair = KeyGenerator.getKeyPairFromSeed(GENESIS_SEED.toByteArray());
//        Block.Body.Builder bodyBuilder = Block.Body.newBuilder();
//        ByteString accountId = GENESIS_ACCOUNT_ID;
//        bodyBuilder.setPreviousBlockHash(ByteString.copyFrom(previousBlockHash));
//        bodyBuilder.setAccountId(accountId);
//        bodyBuilder.setBalance(MAX_SUPPLY - 10000);
//        bodyBuilder.setAmount(10000);
//        bodyBuilder.setDelegatedNodeId(ByteString.copyFrom(Base32Helper.decode("3JEFYDDBOZGXNIL3Z2B3XSD3QI")));
//        bodyBuilder.setVersion(1);
//        bodyBuilder.setSendAccountId(ByteString.copyFrom(toAccountId));
//        bodyBuilder.setBlockType(Block.BlockType.SEND);
//        bodyBuilder.setReferenceCode(ByteString.copyFrom("miha".getBytes()));
//        bodyBuilder.setPublicKeys(Block.PublicKeys.newBuilder()
//                .addPublicKey(ByteString.copyFrom(keyPair.getPublicKey().getPublicKey())));
//
//        Block.Body body = bodyBuilder.build();
//
//        Block.Builder blockBuilder = Block.newBuilder()
//                .setBody(body)
//                .setSignatues(Block.Signatures.newBuilder()
//                        .addSignature(ByteString.copyFrom(signData(keyPair, body.toByteArray())))
//                )
//                .setBlockHash(Block.Hash.newBuilder().setHash(ByteString.copyFrom(HashHelper.sha512(body.toByteArray()))));
//
//        transactionReqBuilder.setBlock(blockBuilder);
//
//        Api.TransactionRes transactionRes = accountServiceStub.transaction(transactionReqBuilder.build());
//        return Response.ok(transactionRes.getSuccess()).build();
//    }
    @GET
    @Path("pay-to/{accountId}/{amount}")
    public Response payTo(@PathParam("accountId") String accountId, @PathParam("amount") Long amount) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, CryptoException {
        AccountServiceGrpc.AccountServiceBlockingStub accountServiceStub = apiClient.getAccountServiceStub();
        
        Api.TransactionReq.Builder transactionReqBuilder = Api.TransactionReq.newBuilder();
        byte[] decodedAccountId = Base32Helper.decode(accountId);
        
        Api.LastBlockReq accountReq = Api.LastBlockReq.newBuilder()
                .setAccountId(GENESIS_ACCOUNT_ID)
                .build();
        
        Api.LastBlockRes lastBlock = accountServiceStub.lastBlock(accountReq);
        
        KeyPair keyPair = KeyGenerator.getKeyPairFromSeed(GENESIS_SEED.toByteArray());
        Block.Body.Builder bodyBuilder = Block.Body.newBuilder();
        bodyBuilder.setPreviousBlockHash(lastBlock.getBlock().getBlockHash().getHash());
        bodyBuilder.setAccountId(GENESIS_ACCOUNT_ID);
        bodyBuilder.setTimestamp(DateHelper.toMillis(ZonedDateTime.now()));
        bodyBuilder.setBalance(lastBlock.getBlock().getBody().getBalance() - amount);
        bodyBuilder.setAmount(amount);
        bodyBuilder.setDelegatedNodeId(ByteString.copyFrom(Base32Helper.decode("3JEFYDDBOZGXNIL3Z2B3XSD3QI")));
        bodyBuilder.setVersion(1);
        bodyBuilder.setSendAccountId(ByteString.copyFrom(decodedAccountId));
        bodyBuilder.setBlockType(Block.BlockType.SEND);
        bodyBuilder.setReferenceCode(ByteString.copyFrom("welcome".getBytes()));
        bodyBuilder.setPublicKeys(Block.PublicKeys.newBuilder()
                .addPublicKey(ByteString.copyFrom(keyPair.getPublicKey().getPublicKey())));
        
        Block.Body body = bodyBuilder.build();
        
        Block.Builder blockBuilder = Block.newBuilder()
                .setBody(body)
                .setSignatues(Block.Signatures.newBuilder()
                        .addSignature(ByteString.copyFrom(signData(keyPair, body.toByteArray())))
                )
                .setBlockHash(Block.Hash.newBuilder().setHash(ByteString.copyFrom(HashHelper.sha512(body.toByteArray()))));
        
        transactionReqBuilder.setBlock(blockBuilder);
        
        Api.TransactionRes transactionRes = accountServiceStub.transaction(transactionReqBuilder.build());
        return Response.ok(transactionRes.getSuccess()).build();
    }
    
    @GET
    @Path("count-accounts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response countAccounts() throws SignatureException, InvalidKeyException, CryptoException, NoSuchAlgorithmException {
        
        AccountServiceGrpc.AccountServiceBlockingStub accountServiceStub = apiClient.getAccountServiceStub();
        
        Api.AccountCountRes numAccounts = accountServiceStub.numAccounts(Api.AccountCountReq.getDefaultInstance());
        return Response.ok(numAccounts.getCount()).build();
    }
    
    @GET
    @Path("new-account")
    @Produces(MediaType.APPLICATION_JSON)
    public Response newAccount() throws SignatureException, InvalidKeyException, CryptoException, NoSuchAlgorithmException {
        AccountServiceGrpc.AccountServiceBlockingStub accountServiceStub = apiClient.getAccountServiceStub();
        
        byte[] seed = new byte[64];
        RandomHelper.get().nextBytes(seed);
        
        Api.TransactionReq.Builder transactionReqBuilder = Api.TransactionReq.newBuilder();
        
        KeyPair keyPair = KeyGenerator.getKeyPairFromSeed(seed);
        
        Block.Body.Builder bodyBuilder = Block.Body.newBuilder();
        ByteString accountId = ByteString.copyFrom(UUIDHelper.asBytes());
        bodyBuilder.setAccountId(accountId);
        bodyBuilder.setBalance(0);
        bodyBuilder.setAmount(0);
        bodyBuilder.setDelegatedNodeId(ByteString.copyFrom(Base32Helper.decode("3JEFYDDBOZGXNIL3Z2B3XSD3QI")));
        bodyBuilder.setVersion(1);
        bodyBuilder.setBlockType(Block.BlockType.CREATE);
        bodyBuilder.setPublicKeys(Block.PublicKeys.newBuilder()
                .addPublicKey(ByteString.copyFrom(keyPair.getPublicKey().getPublicKey())));
        
        Block.Body body = bodyBuilder.build();
        
        Block.Builder blockBuilder = Block.newBuilder()
                .setBody(body)
                .setSignatues(Block.Signatures.newBuilder()
                        .addSignature(ByteString.copyFrom(signData(keyPair, body.toByteArray())))
                )
                .setBlockHash(Block.Hash.newBuilder().setHash(ByteString.copyFrom(HashHelper.sha512(body.toByteArray()))));
        
        LOGGER.info("Seed: {}", Base32Helper.encode(seed));
        LOGGER.info("AccountId: {}", Base32Helper.encode(accountId.toByteArray()));
        
        transactionReqBuilder.setBlock(blockBuilder);
        
        Api.TransactionRes transactionRes = accountServiceStub.transaction(transactionReqBuilder.build());
        return Response.ok(new AccountDto(Base32Helper.encode(seed), Base32Helper.encode(accountId.toByteArray()))).build();
    }
    
    public byte[] signData(KeyPair keyPair, byte[] data) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException {
        EdDSANamedCurveSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
        EdDSAEngine edDSAEngine = new EdDSAEngine(MessageDigest.getInstance(spec.getHashAlgorithm()));
        
        edDSAEngine.initSign(keyPair.getPrivateKey().getEdDSAPrivateKey());
        edDSAEngine.update(data);
        return edDSAEngine.sign();
    }
    
    public static class AccountDto {
        
        public String seed;
        public String accountId;
        
        public AccountDto(String seed, String accountId) {
            this.seed = seed;
            this.accountId = accountId;
        }
        
    }
    
}
