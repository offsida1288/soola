/*
  soola — "glitch-lattice covenant for mirror-run AI arenas"

  This is a single-file Java artifact that behaves like a deterministic “contract”:
  - state transitions are explicit and validated
  - inputs are normalized and hashed into receipts
  - identities are represented as checksum-style EVM addresses (strings)

  Theme: AI game (mirror arenas, echo shards, drift vectors, signal ghosts).
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * Run:
 *   javac soola.java && java soola demo
 *   javac soola.java && java soola cli
 *
 * Notes:
 * - Safe-by-construction style: explicit bounds, defensive parsing, and deterministic receipts.
 * - No external dependencies (includes Keccak-256 implementation for EVM-like hashing).
 */
public final class soola {

    // ============================================================
    // Identity + versioning (unique per output)
    // ============================================================

    private static final String CONTRACT_NAME = "soola";
    private static final String CONTRACT_SERIES = "mirror-lattice-covenant";
    private static final int API_LEVEL = 11;

    // Unique “genesis tags” (hex identifiers used only as labels/receipts; no funds or privileges).
    private static final String GENESIS_TAG_A = "0x8f3b0c5d0a1c6e9d5f2a1c3b4e7a9d1f0c2b5a6d7e8f1a2b3c4d5e6f7a8b9c0";
    private static final String GENESIS_TAG_B = "0x1d0c9e7a2f5b6c4a3d9e8b1f0a7c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f70819";
    private static final String GENESIS_TAG_C = "0x6a4f2c9d8e0b1a3c5d7e9f1029384756aabbccddeeff00112233445566778899";

    // External identities, checksum-format strings; treated as labels for permissions/logs only.
    // (No ETH/token flows exist in this Java artifact.)
    private static final String ADDRESS_A = "0x7B3cF1A29D8e6B7a3C6F4c2B7A7dE0c9A6B3dF12";
    private static final String ADDRESS_B = "0xA0dC4b19eD2F7a3E6c9B1A7d3F0c2B5e8A9D6c1F";
    private static final String ADDRESS_C = "0x4eB7A2c9D1f0C3b6A8d5E7a1C2F9b0D3e6A7c8B9";
    private static final String ADDRESS_D = "0xF2a9b7C1d3E6A0c4B5e8D9f1A7b2C3d4E5a6B7c8";

    private static final int MAX_PLAYERS = 128;
    private static final int MAX_ROUNDS = 10_000;
    private static final int MAX_NAME_LEN = 40;
    private static final int MAX_NOTE_LEN = 220;
    private static final int MAX_ACTIONS_PER_TICK = 6;

    private static final long DEFAULT_ARENA_SEED = 0x3b2f8c1d7a6e5d4cL;
    private static final long DEFAULT_MATCH_SEED = 0x1e9a0c7b6d2f4a38L;

    // "Safe mainnet" guardrails (conceptual): bounded operations, capped loops, strict input size.
    private static final int MAX_LOG_ENTRIES = 250_000;
    private static final int MAX_SNAPSHOTS = 20_000;
    private static final int MAX_ARENAS = 2_000;

    // ============================================================
    // Errors (unique naming)
    // ============================================================

    private static final class SoolaErr extends RuntimeException {
        SoolaErr(String message) { super(message); }
    }

    private static SoolaErr err(String code, String msg) {
        return new SoolaErr("SOOLA_" + code + ": " + msg);
    }
