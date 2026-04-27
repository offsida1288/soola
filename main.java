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
